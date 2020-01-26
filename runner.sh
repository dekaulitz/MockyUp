type=$1
env=$2
envType=$3

#check environment
if [ -z $env ]; then
    ENVIRONMENT=.env.dev
else
  if [ $env == "-e" ]; then
      if [ -z $envType ]; then
          ENVIRONMENT=.env.dev
      else
         ENVIRONMENT=.env.${envType}
      fi
  fi
fi

. $ENVIRONMENT

#run by type
if [ $type == "docker" ]; then
    mvn install
    docker build -t mockup:latest .
    docker run -itd -e DATABASE_HOST=$DATABASE_HOST -e SERVER_PORT=$PORT -p 8080:$PORT mockup:latest
elif [ $type == "run" ]; then
   mvn install
   java -jar ./target/mockyup-0.0.1-SNAPSHOT.jar
 fi

