package utils.price.formula;

public class CanvasFormula implements Formula {
	@Override
	public double calculatePrice(double basePrice) {
		return basePrice * 1.5;
	}
}
