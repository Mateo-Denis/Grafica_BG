//CLASE QUE SE ENCARGA DE QUE EL INPUT DE PRECIO DE PRODUCTO SEA VALIDO (SOLO ADMITE NUMEROS Y/O DECIMALES)

package utils;

import javax.swing.*;
import javax.swing.text.*;

public class ProductPriceInputVerifier extends DocumentFilter {

    // Expresión regular que permite dígitos y un punto decimal
    private static final String NUMERIC_REGEX = "\\d*\\.?\\d*";

    // Este método se llama cuando se inserta texto en el documento, es decir, cuando el usuario escribe.
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        // Si el texto a insertar coincide con la expresión regular, se permite la inserción.
        if (string.matches(NUMERIC_REGEX)) {
            super.insertString(fb, offset, string, attr);
        }
        // Si no coincide, la inserción no se realiza (se ignora la entrada).
    }

    // Este método se llama cuando se reemplaza texto en el documento, por ejemplo, cuando se pega texto.
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        // Si el texto a reemplazar coincide con la expresión regular, se permite el reemplazo.
        if (text.matches(NUMERIC_REGEX)) {
            super.replace(fb, offset, length, text, attrs);
        }
        // Si no coincide, el reemplazo no se realiza.
    }

    // Este método se llama cuando se elimina texto del documento.
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        // La eliminación de texto siempre se permite.
        super.remove(fb, offset, length);
    }
}

/*

Explicación de cada componente:


1) Clase NumericDocumentFilter:

Esta clase extiende DocumentFilter, lo que permite interceptar y modificar las operaciones de inserción, reemplazo y eliminación de texto en un componente de texto (JTextField en este caso).


2) NUMERIC_REGEX:

Es una expresión regular que define lo que se considera una entrada válida. En este caso, \\d*\\.?\\d* significa:
\\d*: Cero o más dígitos.
\\.?: Opcionalmente, un punto.
\\d*: Cero o más dígitos después del punto.
Esto permite tanto números enteros como decimales (por ejemplo, 123, 123.45).


3) Método insertString:

Este método se ejecuta cuando el usuario intenta insertar texto en el campo.

-->Parámetros:
FilterBypass fb: Permite al filtro realizar la operación original si se cumplen las condiciones.
int offset: La posición en el documento donde se insertará el texto.
String string: El texto que se va a insertar.
AttributeSet attr: Atributos asociados al texto.

-->Lógica:
Si el texto a insertar coincide con la expresión regular (NUMERIC_REGEX), se permite la inserción llamando a super.insertString().
Si no coincide, la inserción se ignora.


4) Método replace:

Este método se ejecuta cuando el usuario intenta reemplazar texto en el campo (por ejemplo, cuando pega texto).

-->Parámetros:
FilterBypass fb: Igual que en insertString.
int offset: La posición en el documento donde comenzará el reemplazo.
int length: La longitud del texto que será reemplazado.
String text: El nuevo texto que reemplazará al antiguo.
AttributeSet attrs: Atributos asociados al nuevo texto.

-->Lógica:
Similar a insertString: Si el nuevo texto coincide con la expresión regular, se permite el reemplazo llamando a super.replace().
Si no coincide, se ignora el reemplazo.


5) Método remove:

Este método se ejecuta cuando el usuario intenta eliminar texto del campo.

-->Parámetros:
FilterBypass fb: Igual que en insertString.
int offset: La posición en el documento desde donde comenzará la eliminación.
int length: La longitud del texto que será eliminado.

-->Lógica:
La eliminación siempre se permite, por lo que simplemente se llama a super.remove().

*/
