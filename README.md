# Strangers
Application mobile permettant de retrouver � qui appartient un num�ro de t�l�phone, en faisant une recherche dans des emails.

## Technologies
* Application cliente : Phonegap (Android) / HTML / JavaScript
* Application serveur : PHP

## Installation
* Serveur : Copier le dossier "Strangers_Serveur" sur un FTP, Wamp, EasyPHP, ...
* Client (Android) : Cr�er un projet Android (sur NetBeans), et compiler

## Mise en production
### Ne pas afficher les erreurs PHP
* (Cela pourrait poser probleme lors du retour de la r�ponse sur l'appli)
* Aller dans PHP.ini : chercher la ligne "display_errors = On"
* Mettre "display_errors = Off"