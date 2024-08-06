package utils;

import javax.swing.*;

public enum MessageTypes {

    CLIENT_CREATION_SUCCESS("Cliente creado con éxito"
            , "El cliente fue agregado a la base de datos correctamente."
            , JOptionPane.INFORMATION_MESSAGE),
    CLIENT_CREATION_FAILURE("Error al crear el cliente"
            , "Ha ocurrido un error al intentar crear el cliente."
            , JOptionPane.ERROR_MESSAGE),
    CLIENT_SEARCH_FAILURE("Error al buscar el cliente"
            , "Ha ocurrido un error al intentar buscar el cliente."
            , JOptionPane.ERROR_MESSAGE),
    CITY_FETCH_FAILURE("Error al consultar las ciudades"
            , "Ha ocurrido un error al consultar las ciudades en la base de datos."
            , JOptionPane.ERROR_MESSAGE),
    PRODUCT_CREATION_SUCCESS("Producto creado con éxito"
            , "El producto fue agregado a la base de datos correctamente."
            , JOptionPane.INFORMATION_MESSAGE),
    PRODUCT_CREATION_FAILURE("Error al crear el producto"
            , "Ha ocurrido un error al intentar crear el producto."
            , JOptionPane.ERROR_MESSAGE),
    PRODUCT_SEARCH_FAILURE("Error al buscar el producto"
            , "Ha ocurrido un error al intentar buscar el producto."
            , JOptionPane.ERROR_MESSAGE),
    BUDGET_SEARCH_FAILURE("Error al buscar el presupuesto"
            , "Ha ocurrido un error al intentar buscar el presupuesto."
            , JOptionPane.ERROR_MESSAGE),
    BUDGET_CREATION_SUCCESS("Presupuesto creado con éxito"
            , "El presupuesto fue agregado a la base de datos correctamente."
            , JOptionPane.INFORMATION_MESSAGE),
    BUDGET_CREATION_FAILURE("Error al crear el presupuesto"
            , "Ha ocurrido un error al intentar crear el presupuesto."
            , JOptionPane.ERROR_MESSAGE),
    EMPTY_RATING_SAVE_ATTEMPT("Error while saving rating"
            , "Cannot save a rating for an empty page. Please search for a page first."
            , JOptionPane.ERROR_MESSAGE),
    EMPTY_PAGE_SAVE_ATTEMPT("Error while saving page"
            , "Cannot save an empty page. Please search for a page first."
            , JOptionPane.ERROR_MESSAGE),
    ACCESS_FAILURE("Error while accessing database"
            , "An error occurred while accessing the database"
            , JOptionPane.ERROR_MESSAGE),
    PAGE_DELETE_SUCCESS("Page deleted successfully"
            , "The page was successfully deleted from the database"
            , JOptionPane.INFORMATION_MESSAGE),
    PAGE_DELETE_FAILURE("Error while deleting page"
            , "An error occurred while deleting the page from the database"
            , JOptionPane.ERROR_MESSAGE),
    PAGE_SEARCH_FAILURE("Error while searching page"
            , "An error occurred while searching the page from the Wikipedia API"
            , JOptionPane.ERROR_MESSAGE);

    private final String title;
    private final String message;
    private final int messageType;

    MessageTypes(String title, String message, int messageType) {
        this.title = title;
        this.message = message;
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageType() {
        return messageType;
    }

}
