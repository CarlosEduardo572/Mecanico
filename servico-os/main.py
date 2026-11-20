from fastapi import FastAPI

app = FastAPI()

@app.get("/ordens")
def listar_ordens():
    return {"mensagem": "Lista de Ordens de Serviço (em breve do banco de dados!)"}