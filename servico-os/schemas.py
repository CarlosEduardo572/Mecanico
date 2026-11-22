from pydantic import BaseModel, ConfigDict

class OrdemServicoCreate(BaseModel):
    cliente_id: int
    cliente_nome: str
    placa_veiculo: str
    descricao_servico: str
    valor: float

class OrdemServicoUpdateStatus(BaseModel):
    status: str

class OrdemServicoResponse(BaseModel):
    id: int
    cliente_id: int
    cliente_nome: str
    placa_veiculo: str
    descricao_servico: str
    valor: float
    status: str

    model_config = ConfigDict(from_attributes=True)

# ---> NOVO ESQUEMA PARA ADICIONAR PEÇAS <---
class UsoDePeca(BaseModel):
    peca_id: int
    quantidade: int