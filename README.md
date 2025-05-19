# 📧 Générateur d'emails professionnels

## 🔎 Aperçu
Cette application automatise la rédaction d'emails professionnels à l'aide de l'API OpenAI (**GPT-3.5-turbo-instruct**), intégrée via **JavaFX** pour une interface graphique intuitive et **Maven** pour la gestion des dépendances.  
Elle permet de **générer des emails adaptés** au contexte, destinataire, objet et ton (formel ou amical) en **1 à 2 secondes**, avec une option de **copie dans le presse-papiers**.

---

## ⚙️ Fonctionnalités

- **Saisie** : Entrez le **contexte**, le **destinataire**, l'**objet**, et choisissez un **ton** (formel/amical).
- **Génération** : Produit des emails via **l'API OpenAI**, optimisés pour éviter les fragments.
- **Copie** : Transfère l'email généré dans le **presse-papiers** avec **confirmation visuelle**.
- **Interface** : Design moderne avec **JavaFX** et **CSS** *(voir interface_email_formel.png)*.
- **Robustesse** : Gère les erreurs (**champs vides, pannes d'API**).

---

## 📂 Structure du projet
```
EmailGeneratorProject/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── generateurAutomatiqueTexteGPT/
│   │   │       ├── EmailGeneratorApp.java
│   │   │       ├── OpenAiHandler.java
│   │   ├── resources/
│   │       └── styles.css
├── pom.xml
├── README.md
```

---

## 🛠 Prérequis

- **Java 17**
- **Maven**
- **Clé API OpenAI** *(voir platform.openai.com)*
- **JavaFX 24**

---

## 🚀 Installation

### **1. Clonez le dépôt**
```bash
git clone https://github.com/Marc1T/GenerateurAutomatiqueDeTexteEnJavaGPT.git
cd EmailGeneratorProject
```

### **2. Configurez la clé API OpenAI**
🔑 Obtenez une clé sur **platform.openai.com**  
Ajoutez-la comme variable d'environnement :
```bash
export OPENAI_API_KEY='votre-clé'
```

### **3. Compilez et exécutez**
```bash
mvn clean install
mvn javafx:run
```

---

## 🎯 Utilisation

1. Lancez l'application avec `mvn javafx:run`
2. Entrez :
   - **Contexte** : Ex. *"Demander une prolongation"*
   - **Destinataire** : Ex. *"Professeur Hajji"*
   - **Objet** : Ex. *"Demande de délai"*
   - **Ton** : *Formel* ou *Amical*
3. Cliquez sur **"Générer"** pour produire l'email.
4. Cliquez sur **"Copier"** pour transférer l'email dans le **presse-papiers** *(voir confirmation_copie.png)*.

---

## 📦 Dépendances

- **openai-java 0.18.2**
- **JavaFX 24**
- **Maven** *(voir pom.xml dans pom_xml.png)*

---

## 🎗️ Remerciements
Un grand merci à **Ravel** pour avoir fourni la **clé API OpenAI**, essentielle à la réalisation de ce projet. 🙌

---

## ✍️ Auteur
**Marc Thierry NANKOULI**  
Projet académique, **19 mai 2025**
