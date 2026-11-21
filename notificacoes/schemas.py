from datetime import datetime
from pydantic import BaseModel, ConfigDict

class NotificacaoCreate(BaseModel):
    ordem_id: int
    cliente_nome: str
    mensagem: str

class NotificacaoResponse(BaseModel):
    id: int
    ordem_id: int
    cliente_nome: str
    mensagem: str
    data_envio: datetime

    model_config = ConfigDict(from_attributes=True)