# 💬 JavaFX Multithreaded Chat App (Client / Server)

![Java](https://img.shields.io/badge/Java-%2B-blue?style=flat&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-UI-green?style=flat)
![Socket](https://img.shields.io/badge/Socket-Multithreading-yellow)
![Status](https://img.shields.io/badge/Status-Stable-brightgreen)

## 📌 Description

Une application de chat en **Java** avec interface **JavaFX**, utilisant des **sockets TCP multithreads** côté serveur. Chaque client peut :

- Envoyer/recevoir des messages en temps réel
- Voir les messages alignés selon l’auteur (gauche/droite)
- Visualiser les logs dans la console (connexions, déconnexions, messages)
- Affichage d’un message lorsque quelqu’un rejoint ou quitte

## 📷 Capture d’écran

![Chat Demo](/screenshoots/ChatMultithreads.png)

## ⚙️ Fonctionnalités principales

- 🧵 **Multithread Server** : Chaque client a son propre thread.
- 💬 **Interface JavaFX** côté client.
- 📜 **Logs serveur** : Connexions, messages, déconnexions.
- 🎨 Messages alignés :
- Gauche : messages des autres
- Droite : messages de soi-même
- 🔌 Communication via `Socket` et `BufferedReader/Writer`.


## 📁 Structure du projet
```
📦 src
 └── 📁 net.example.chatappwebsockets
     └── 📁 app
         ├── 📄 Chat.java             # Interface graphique Chat(fenêtre utilisateur)
         ├── 📄 Client.java           # Classe de démarrage du client
         ├── 📄 Server.java           # Classe principale du serveur (écoute les sockets)
         ├── 📄 ServerMultiThread.java# Lance le serveur multiclients avec threads
         └── 📄 SocketThread.java     # Thread individuel pour chaque client connecté
```
