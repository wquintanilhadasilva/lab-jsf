
Laboratório para fornecer via spring-boot, receber e fazer download via JBOSS de arquivos grandes, LARGE SIZE. Testes com arquivo de 13GB com sucesso!


# Para acessar a aplicação:

[http://localhost:8080/project-0.0.1-SNAPSHOT/test.xhtml](http://localhost:8080/project-0.0.1-SNAPSHOT/test.xhtml)


# Utilitário para realizar o download:


```
sudo apt-get install pdftk
```

## Para unir os arquivos

O comando abaixo faz uma iteração de cem vezes e une o arquivo **origem.pdf** em um arquivo **260_MB.pdf** final.

```
pdftk $(for i in $(seq 1 100); do echo -n "origem.pdf ";^Cone) cat output 260_MB.pdf
```