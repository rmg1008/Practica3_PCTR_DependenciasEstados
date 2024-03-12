package src.p03.c01;

public class SistemaLanzador {
	private final static int NUM_PUERTAS = 5; // Valor por defecto
	
	public static void main(String[] args) 
	{
		int nPuertas = NUM_PUERTAS; 
		
		// Se toma el valor si se pasa por parámetro
		if (args != null && args.length > 0)
		{
			nPuertas = Integer.parseInt(args[0]);
		}
		IParque parque = new Parque();  // Instanciamos clase Parque
		char letra_puerta = 'A';
		
		System.out.println("¡Parque abierto!");
		
		for (int i = 0; i < nPuertas; i++) 
		{	
			String puerta = ""+(letra_puerta++);
			
			// Creación de hilos de entrada
			ActividadEntradaPuerta entradas = new ActividadEntradaPuerta(puerta, parque);
			new Thread (entradas).start();
			
			// Creación de hilos de salida
			ActividadSalidaPuerta salidas = new ActividadSalidaPuerta(puerta, parque);
			new Thread (salidas).start();
			
		}
	}
}