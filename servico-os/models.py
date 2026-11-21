from sqlalchemy import Column, Integer, String, Float
from database import Base

class OrdemServico(Base):
    __tablename__ = "ordens_servico"

    id = Column(Integer, primary_key=True, index=True)
    cliente_nome = Column(String, index=True)
    placa_veiculo = Column(String, index=True)
    descricao_servico = Column(String)
    valor = Column(Float)
    status = Column(String, default="Aberta")