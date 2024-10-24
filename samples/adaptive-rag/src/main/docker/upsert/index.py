### Build Index

import chromadb
from chromadb.config import Settings

from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_community.document_loaders import WebBaseLoader
from langchain_community.vectorstores import Chroma
from langchain_openai import OpenAIEmbeddings

## see https://stackoverflow.com/a/77925278/521197
class CustomOpenAIEmbeddings(OpenAIEmbeddings):
    "make OpenAIEmbeddings compliant with chromadb api"

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

    def _embed_documents(self, texts):
        return super().embed_documents(texts)  # <--- use OpenAIEmbedding's embedding function

    def __call__(self, input):
        return self._embed_documents(input)    # <--- get the embeddings


def upsert_chroma_via_http( client, docs, embedding_function ):
    import uuid

    client.reset()  # resets the database
    collection = client.create_collection("rag-chroma", embedding_function=CustomOpenAIEmbeddings())
    for doc in docs:
        collection.add(
            ids=[str(uuid.uuid1())],
            metadatas=doc.metadata,
            documents=doc.page_content
        )
    return client

def upsert_chroma_local():
    "Add to vectorstore"

    vectorstore = Chroma.from_documents(
        documents=doc_splits,
        collection_name="rag-chroma",
        embedding=embd,
     )
    retriever = vectorstore.as_retriever()

def test_query( client, embedding_function ):

    db4 = Chroma(
        client=client,
        collection_name="rag-chroma",
        embedding_function=embedding_function,
    )
    query = "What are the types of agent memory?"
    docs = db4.similarity_search(query)
    print(docs[0].page_content)


# Set embeddings
embd = OpenAIEmbeddings()

# Docs to index
urls = [
    "https://lilianweng.github.io/posts/2023-06-23-agent/",
    "https://lilianweng.github.io/posts/2023-03-15-prompt-engineering/",
    "https://lilianweng.github.io/posts/2023-10-25-adv-attack-llm/",
]

# Load
docs = [WebBaseLoader(url).load() for url in urls]
docs_list = [item for sublist in docs for item in sublist]

# Split
text_splitter = RecursiveCharacterTextSplitter.from_tiktoken_encoder(
    chunk_size=500, chunk_overlap=0
)
doc_splits = text_splitter.split_documents(docs_list)

client = chromadb.HttpClient( host="chromadb", port=8000, settings=Settings(allow_reset=True))

upsert_chroma_via_http( client, doc_splits, embd )

test_query( client, embd)