from datetime import datetime
from sqlalchemy import Column, Integer, String, DateTime
from database import Base

class Notificacao(Base):
    __tablename__ = "notificacoes"

    id = Column(Integer, primary_key=True, index=True)
    ordem_id = Column(Integer, index=True)
    cliente_nome = Column(String)
    mensagem = Column(String)
    data_envio = Column(DateTime, default=datetime.now)