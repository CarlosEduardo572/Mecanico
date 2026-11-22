import os
from fastapi import FastAPI, Depends, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session
from typing import List
import requests

import models, schemas
from database import engine, SessionLocal

models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="Microsserviço de Ordens de Serviço")

# Libera o front-end (outro endereço) para chamar este serviço
app.add_middleware(CORSMiddleware, allow_origins=["*"], allow_methods=["*"], allow_headers=["*"])

# Endereços dos outros serviços. Na nuvem, definidos por variável de ambiente.
CLIENTES_URL = os.getenv("CLIENTES_URL", "http://localhost:8080")
CATALOGO_URL = os.getenv("CATALOGO_URL", "http://localhost:8081")
NOTIFICACOES_URL = os.getenv("NOTIFICACOES_URL", "http://127.0.0.1:8002")

def get_db():
    db = SessionLocal()
    try: yield db
    finally: db.close()

# COMUNICAÇÃO COM JAVA - CLIENTES
def verificar_cliente_java(cliente_id: int):
    try:
        resposta = requests.get(f"{CLIENTES_URL}/clientes/{cliente_id}", timeout=30)
        if resposta.status_code == 200:
            return resposta.json()
        return None
    except requests.exceptions.RequestException:
        print("Erro: Não foi possível ligar ao serviço de clientes.")
        return None

# COMUNICAÇÃO COM PYTHON - NOTIFICAÇÕES
def notificar(ordem_id: int, cliente_nome: str, mensagem: str):
    try:
        requests.post(f"{NOTIFICACOES_URL}/notificacoes", json={
            "ordem_id": ordem_id,
            "cliente_nome": cliente_nome,
            "mensagem": mensagem
        }, timeout=30)
    except Exception:
        print("[AVISO] Serviço de notificações fora do ar.")

@app.get("/ordens", response_model=List[schemas.OrdemServicoResponse])
def listar_ordens(db: Session = Depends(get_db)):
    return db.query(models.OrdemServico).order_by(models.OrdemServico.id.desc()).all()

@app.post("/ordens", response_model=schemas.OrdemServicoResponse)
def criar_ordem(ordem: schemas.OrdemServicoCreate, db: Session = Depends(get_db)):
    cliente = verificar_cliente_java(ordem.cliente_id)
    if not cliente:
        raise HTTPException(status_code=404, detail="Cliente não encontrado no sistema Java.")

    nova_ordem = models.OrdemServico(**ordem.model_dump(), status="Aberta")
    db.add(nova_ordem)
    db.commit()
    db.refresh(nova_ordem)
    notificar(nova_ordem.id, nova_ordem.cliente_nome, f"OS #{nova_ordem.id} aberta com sucesso.")
    return nova_ordem

@app.patch("/ordens/{ordem_id}/status", response_model=schemas.OrdemServicoResponse)
def atualizar_status(ordem_id: int, dados: schemas.OrdemServicoUpdateStatus, db: Session = Depends(get_db)):
    ordem = db.query(models.OrdemServico).filter(models.OrdemServico.id == ordem_id).first()
    if not ordem: raise HTTPException(status_code=404, detail="Não encontrada")
    ordem.status = dados.status
    db.commit()
    notificar(ordem.id, ordem.cliente_nome, f"Status da OS #{ordem.id} mudou para: {ordem.status}")
    return ordem

# COMUNICAÇÃO COM JAVA - CATÁLOGO E ESTOQUE
@app.post("/ordens/{ordem_id}/pecas")
def adicionar_peca(ordem_id: int, dados: schemas.UsoDePeca, db: Session = Depends(get_db)):
    ordem = db.query(models.OrdemServico).filter(models.OrdemServico.id == ordem_id).first()
    if not ordem:
        raise HTTPException(status_code=404, detail="Ordem de Serviço não encontrada.")

    try:
        url_java = f"{CATALOGO_URL}/pecas/{dados.peca_id}/estoque"
        payload = {"quantidade": -dados.quantidade}

        resposta = requests.patch(url_java, json=payload, timeout=30)

        if resposta.status_code == 404:
            raise HTTPException(status_code=404, detail="Peça não encontrada no catálogo Java.")
        if resposta.status_code == 409:
            raise HTTPException(status_code=400, detail="Estoque insuficiente no Java para essa quantidade.")
        if resposta.status_code not in [200, 201, 204]:
            raise HTTPException(status_code=400, detail=f"Erro ao descontar peça no estoque do Java (Status {resposta.status_code}).")

    except requests.exceptions.RequestException:
        raise HTTPException(status_code=503, detail="Serviço de Estoque (Java) indisponível.")

    notificar(ordem.id, ordem.cliente_nome, f"Usadas {dados.quantidade} unidades da peça ID {dados.peca_id}.")

    return {"mensagem": "Peça adicionada e estoque atualizado com sucesso!"}