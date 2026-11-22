from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from typing import List

import models, schemas
from database import engine, SessionLocal

models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="Microsserviço de Notificações")
from fastapi.middleware.cors import CORSMiddleware
app.add_middleware(CORSMiddleware, allow_origins=["*"], allow_methods=["*"], allow_headers=["*"])

def get_db():
    db = SessionLocal()
    try: yield db
    finally: db.close()

@app.post("/notificacoes", response_model=schemas.NotificacaoResponse)
def receber_notificacao(dados: schemas.NotificacaoCreate, db: Session = Depends(get_db)):
    nova = models.Notificacao(**dados.model_dump())
    db.add(nova)
    db.commit()
    db.refresh(nova)
    return nova

@app.get("/notificacoes", response_model=List[schemas.NotificacaoResponse])
def listar_notificacoes(db: Session = Depends(get_db)):
    return db.query(models.Notificacao).order_by(models.Notificacao.id.desc()).all()