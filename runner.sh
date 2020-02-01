#!/usr/bin/env bash

set +o histexpand
#set variable from args
for i in "$@"; do
  case ${i} in
  #check environment
  -env=* | --environment=*)
    ENVIRONMENT="${i#*=}"
    shift # past argument=value
    ;;
    #check docker command
  -df=* | --dockerfile=*)
    DOCKERFILE="${i#*=}"
    shift # past argument=value
    ;;
    #
  run=*)
    RUN="${i#*=}"
    shift # past argument=value
    ;;
  *) ;;
  esac
done

CheckFile() {
  if [ ! -z "${ENVIRONMENT+x}" ]; then
    echo "load configuration from "${ENVIRONMENT}
    ENVIRONMENT=".env."${ENVIRONMENT}
  else
    echo "-env or --environment argument not available, we use default variable value 'dev' for now!!"
    ENVIRONMENT=".env.dev"
  fi
  . $ENVIRONMENT
}

RunningAsJar() {
  mvn install
  java -jar ./target/mockyup-0.0.1-SNAPSHOT.jar
  exit
}
BuildImageDocker() {
  mvn install
  if [ -z "${DOCKERFILE+x}" ]; then
    docker build -t $APPLICATION_NAME:latest .
  else
    docker build -t ${APPLICATION_NAME}:latest -f ${DOCKERFILE} .
  fi
}

RunningAsDocker() {
  BuildImageDocker
  docker run -it -e DATABASE_HOST=$DATABASE_HOST -e SERVER_PORT=$SERVER_PORT -e WEB_LOG_LEVEL=$WEB_LOG_LEVEL -e LOG_LEVEL=$LOG_LEVEL -p 8080:$SERVER_PORT mockup:latest
}

#check application command run is exist or no
if [ ! -z ${RUN+x} ]; then
  CheckFile
  echo "running application as " ${RUN}
  if [ "$RUN" == "jar" ]; then
    RunningAsJar
  elif [ $RUN == "docker" ]; then
    RunningAsDocker
  elif [ $RUN == "build-image" ]; then
    BuildImageDocker
  fi
else
  CheckFile
  echo "argument not available, we use run jar as default for now!!"
  RunningAsJar
fi
