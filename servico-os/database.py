from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base

# Cria um arquivo de banco de dados local chamado oficina.db
SQLALCHEMY_DATABASE_URL = "sqlite:///./oficina.db"

engine = create_engine(
    SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False}
)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()