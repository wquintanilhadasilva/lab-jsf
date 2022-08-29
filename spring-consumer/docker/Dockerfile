FROM openjdk:11.0-jre 
RUN useradd -ms /bin/bash appuser
WORKDIR /home/appuser

VOLUME /opt/mpe
VOLUME /opt/ambiente
VOLUME /opt/certs

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown appuser:appuser entrypoint.sh

COPY spring-consumer-1.0-SNAPSHOT.jar /home/appuser/app.jar
RUN chown appuser:appuser app.jar

COPY --chown=appuser:appuser ./opt /opt

RUN mkdir $JAVA_HOME/lib/fonts

COPY ./fonts /usr/local/share/fonts
RUN fc-cache -f -v
RUN cp /usr/local/share/fonts/*.* $JAVA_HOME/lib/fonts

USER appuser

EXPOSE 9532

ENTRYPOINT ["./entrypoint.sh"]