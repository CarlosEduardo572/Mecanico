from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List

import models
import schemas
from database import engine, SessionLocal

# Cria as tabelas no arquivo oficina.db automaticamente ao iniciar
models.Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="Microsserviço de Ordens de Serviço e Notificações",
    description="API responsável pelo gerenciamento de OS e alertas da oficina mecânica.",
    version="1.0.0"
)

# Abre e fecha a conexão com o banco de dados a cada requisição
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.post("/ordens", response_model=schemas.OrdemServicoResponse, status_code=201, tags=["Ordens de Serviço"])
def criar_ordem(ordem: schemas.OrdemServicoCreate, db: Session = Depends(get_db)):
    nova_ordem = models.OrdemServico(
        cliente_nome=ordem.cliente_nome,
        placa_veiculo=ordem.placa_veiculo,
        descricao_servico=ordem.descricao_servico,
        valor=ordem.valor,
        status="Aberta"
    )
    db.add(nova_ordem)
    db.commit()
    db.refresh(nova_ordem)
    return nova_ordem

@app.get("/ordens", response_model=List[schemas.OrdemServicoResponse], tags=["Ordens de Serviço"])
def listar_ordens(db: Session = Depends(get_db)):
    return db.query(models.OrdemServico).all()

@app.get("/ordens/{ordem_id}", response_model=schemas.OrdemServicoResponse, tags=["Ordens de Serviço"])
def buscar_ordem(ordem_id: int, db: Session = Depends(get_db)):
    ordem = db.query(models.OrdemServico).filter(models.OrdemServico.id == ordem_id).first()
    if not ordem:
        raise HTTPException(status_code=404, detail="Ordem de Serviço não encontrada")
    return ordem

@app.patch("/ordens/{ordem_id}/status", response_model=schemas.OrdemServicoResponse, tags=["Ordens de Serviço"])
def atualizar_status(ordem_id: int, dados: schemas.OrdemServicoUpdateStatus, db: Session = Depends(get_db)):
    ordem = db.query(models.OrdemServico).filter(models.OrdemServico.id == ordem_id).first()
    if not ordem:
        raise HTTPException(status_code=404, detail="Ordem de Serviço não encontrada")
    
    ordem.status = dados.status
    db.commit()
    db.refresh(ordem)
    return ordem

@app.delete("/ordens/{ordem_id}", tags=["Ordens de Serviço"])
def deletar_ordem(ordem_id: int, db: Session = Depends(get_db)):
    ordem = db.query(models.OrdemServico).filter(models.OrdemServico.id == ordem_id).first()
    if not ordem:
        raise HTTPException(status_code=404, detail="Ordem de Serviço não encontrada")
    
    db.delete(ordem)
    db.commit()
    return {"mensagem": f"Ordem de Serviço #{ordem_id} removida com sucesso!"}