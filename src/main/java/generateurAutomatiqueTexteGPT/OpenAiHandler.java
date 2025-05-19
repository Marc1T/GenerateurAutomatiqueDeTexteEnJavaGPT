package generateurAutomatiqueTexteGPT;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

/**
 * Classe pour gérer les interactions avec l'API OpenAI.
 * Fournit une méthode pour générer des emails professionnels en fonction du contexte,
 * du destinataire, de l'objet et du ton.
 */

public class OpenAiHandler {
    private final OpenAiService service;

    /**
     * Constructeur qui initialise le service OpenAI avec la clé API.
     * La clé est lue depuis la variable d’environnement OPENAI_API_KEY.
     * @throws IllegalStateException si la clé API est absente ou vide.
     */
    public OpenAiHandler() {
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("Clé API OpenAI non trouvée. Veuillez définir la variable d’environnement OPENAI_API_KEY.");
        }
        this.service = new OpenAiService(apiKey);
    }

    /**
     * Génère un email professionnel en utilisant l’API OpenAI.
     * @param context Le contexte de l’email (ex. : "demander une prolongation").
     * @param recipient Le destinataire (ex. : "Professeur Dupont").
     * @param subject L’objet de l’email (ex. : "Demande de prolongation").
     * @param tone Le ton de l’email (ex. : "Formel").
     * @return Le texte de l’email généré, commençant par "Objet :".
     * @throws Exception En cas d’erreur lors de l’appel à l’API.
     */
    public String generateEmail(String context, String recipient, String subject, String tone) throws Exception {
        StringBuilder prompt = new StringBuilder("Rédige un email professionnel en français avec un ton ");
        prompt.append(tone.toLowerCase())
              .append(". Commence directement par 'Objet :' suivi de l'email complet. Contexte : ")
              .append(context);
        if (!recipient.isEmpty()) {
            prompt.append(". Adresse l'email à : ").append(recipient);
        }
        if (!subject.isEmpty()) {
            prompt.append(". Utilise cet objet : ").append(subject);
        } else {
            prompt.append(". Génère un objet approprié.");
        }

        CompletionRequest request = CompletionRequest.builder()
            .model("gpt-3.5-turbo-instruct")
            .prompt(prompt.toString())
            .maxTokens(1000)
            .temperature(0.7)
            .build();
        String response = service.createCompletion(request).getChoices().get(0).getText().trim();

        // Nettoyer la réponse pour supprimer tout texte avant "Objet :"
        int index = response.indexOf("Objet :");
        if (index >= 0) {
            response = response.substring(index);
        } else {
            // Si "Objet :" n'est pas trouvé, ajouter un objet par défaut
            response = "Objet : " + (subject.isEmpty() ? "Demande" : subject) + "\n\n" + response;
        }

        return response.trim();
    }
}