from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
import requests

import models, schemas
from database import engine, SessionLocal

models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="Microsserviço de Ordens de Serviço")

def get_db():
    db = SessionLocal()
    try: yield db
    finally: db.close()

def notificar(ordem_id: int, cliente_nome: str, mensagem: str):
    try:
        # Envia os dados para a porta 8002 onde estará o serviço de notificações
        requests.post("http://127.0.0.1:8002/notificacoes", json={
            "ordem_id": ordem_id,
            "cliente_nome": cliente_nome,
            "mensagem": mensagem
        })
    except:
        print("[AVISO] Serviço de notificações fora do ar.")

@app.post("/ordens", response_model=schemas.OrdemServicoResponse)
def criar_ordem(ordem: schemas.OrdemServicoCreate, db: Session = Depends(get_db)):
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