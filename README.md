# Pharmacy Project

## Description

Le projet "Pharmacy" est une application en Java permettant de gérer une pharmacie, avec des fonctionnalités d'administration et de gestion des produits. L'application propose différentes options en fonction des rôles d'utilisateur (`admin`, `employee` ou `client`). Elle permet aux utilisateurs de gérer les produits, d'ajouter et de supprimer des utilisateurs,de passer des commandes, ainsi que d'afficher les produits en faible stock.

## Fonctionnalités

### Menu Admin
- Ajouter un utilisateur
- Supprimer un utilisateur

### Menu Employee (Accessible pour les employés et les admins)
- Ajouter un produit à la pharmacie
- Supprimer un produit de la pharmacie
- Afficher les produits en faible stock

### Menu des commandes
- nom de la commande
- urgente ou non
- ajouter / supprimer des produits dans la commande
- vérifier et créer la commande

### Menu Principal
- Se déconnecter / connecter
- Voir les produits
- Accéder au menu des commandes
- Quitter l'application

## Prérequis

Avant de lancer le projet, vous devez installer la bibliothèque suivante pour la gestion de JSON :

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>

```
### Version
- java 8

### Installation

```bash
git clone https://github.com/votre-utilisateur/pharmacy.git
```

## Auteurs

Josselin https://github.com/Joss-inf/
Lucas https://github.com/Lucas-Mathieu/
