# Front-end Timelio

Front-end de l'application Timelio réalisé avec Angular (12)

## Installation
* Dans le fichier [environnement](src/app/environments/environment.prod.ts), remplacer l'attribut 'url' par l'url 
du serveur.
* Installer les dependances : npm install
* Construire l'application ; ng build

Les fichiers de l'application se trouvent dans le dossier dist/timelio-front

## Déploiement
Déplacer le contenu du dossier dist/timelio-front dans un serveur web (apache, nginx, etc) pour pouvoir 
y acceder.

*Remarque*: Il faut mettre en place une redirection vers le fichier index.html 
([plus d'informations ici](https://angular.io/guide/deployment#server-configuration))
