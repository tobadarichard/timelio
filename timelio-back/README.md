# Back-end Timelio

Back-end de l'application Timelio réalisé avec Java (11) et Spring Boot

## Installation
### Configuration
Remplir les propriétés vides du fichier [application.properties](src/main/resources/application.properties).  
Remarques: 

* spring.datasource.password/username/url : à configurer pour une base de données Postgresql
* timelio.secret : clé secrete qui doit être encodée en BASE64. Elle est utilisée pour la signature des tokens. Elle doit être accepté par jjwt (voir [ici](https://github.com/jwtk/jjwt#creating-safe-keys) pour savoir comment créer une clé)
* timelio.url-front : l'url du front-end (ex : www.google.com)
* il faut configurer les propriétés spring.mail pour que l'envoi des mails (confirmer compte, demander un nouveau mot de passe) fonctionne
* (facultatif) timelio.default-verify : un boolean qui déterminé si les comptes sont vérifiés par défaut (aucun mail de confirmation ne sera envoyé). Par défaut les comptes doivent être vérifiés
* (facultatif) server.port : le port sur lequel sera disponible l'application (8080 par défaut)

### Compilation
Se placer dans ce dossier et lancer la commande suivante :  
**./mvnw -Dmaven.test.skip=true clean package** sous Linux  
**mvnw.cmd -Dmaven.test.skip=true clean package** sous Windows  

L'application est disponible sous forme de jar executable à l'emplacement target/timelio-back.jar  
Pour le lancer : java -jar target/timelio-back.jar

*Remarque* : pour changer une propriété à l'execution, ajouter un argument de la forme : -Dma.propriete=valeur


## Utilisation

Voir le fichier [api-docs.yaml](api-docs.yaml) qui contient une description de l'API selon la spécification [OpenApi](https://swagger.io/docs/specification/about/)  
On peut visualiser les différentes opérations en important ce fichier dans [l'editeur swagger](https://editor.swagger.io/)
