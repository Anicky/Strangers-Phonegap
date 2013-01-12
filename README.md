# Strangers
Application mobile permettant de retrouver à qui appartient un numéro de téléphone, en faisant une recherche dans des emails.

## Technologies
* Application cliente : Phonegap (Android) / HTML / JavaScript
* Application serveur : PHP

## Installation
* Serveur : Copier le dossier "Strangers_Serveur" sur un FTP, Wamp, EasyPHP, ...
* Client (Android) : Créer un projet Android (sur NetBeans), et compiler

## Mise en production
### Ne pas afficher les erreurs PHP
* (Cela pourrait poser probleme lors du retour de la réponse sur l'appli)
* Aller dans PHP.ini : chercher la ligne "display_errors = On"
* Mettre "display_errors = Off"