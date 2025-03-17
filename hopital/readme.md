# Application de Gestion des Patients

Une application Spring Boot pour la gestion des patients d'un hôpital.

## Technologies Utilisées

- Spring Boot 3.4.3
- Spring Data JPA
- Thymeleaf
- H2 Database
- Bootstrap 5
- Lombok
- Validation

## Fonctionnalités

- Liste paginée des patients
- Recherche des patients par nom
- Ajout de nouveaux patients
- Validation des données
- Interface responsive

## Captures d'écran

### Page d'accueil - Liste des patients
![Page d'accueil](screenshots/index.png)

### Formulaire d'ajout
![Formulaire d'ajout](screenshots/form.png)

## Structure du Projet

```
hopital/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/souhailbektachi/hopital/
│   │   │       ├── entities/
│   │   │       ├── repositories/
│   │   │       └── web/
│   │   └── resources/
│   │       └── templates/
└── pom.xml
```

## Configuration

La base de données H2 est configurée en mémoire avec les paramètres suivants :
- URL : jdbc:h2:mem:hospital-db
- Username : sa
- Password : (vide)
- Console H2 : http://localhost:8080/h2-console

## Installation et Démarrage

1. Cloner le projet
```bash
git clone <url-du-projet>
```

2. Naviguer vers le dossier du projet
```bash
cd hopital
```

3. Lancer l'application
```bash
./mvnw spring-boot:run
```

L'application sera accessible à l'adresse : http://localhost:8080

## Modèle de Données

### Patient
- id : Long (Clé primaire)
- nom : String (4-40 caractères)
- dateNaissance : Date
- malade : boolean
- score : int (0-100)

## Auteur

Souhail Bektachi
