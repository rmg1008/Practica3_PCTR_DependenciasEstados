package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{
	private static final int MIN = 0;  // mínimo valor permitido
	private static final int MAX = 50;  // máximo valor permitido
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	private long tInicial;
	private double tmedio;
	
	/*
	 * Inicialización de variables en constructor
	 */
	public Parque() 
	{
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<>();
		tInicial = System.currentTimeMillis();
		tmedio = 0;
	}


	/*
	 * 
	 */
	@Override
	public synchronized void entrarAlParque(String puerta)
	{
		comprobarAntesDeEntrar(); //check para determinar si el hilo puede continuar
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		
		long tActual = System.currentTimeMillis();
		tmedio = (tmedio + (tActual - tInicial)) / 2.0;
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		
		notifyAll();  // Se notifica al resto de hilos
		
		checkInvariante();
		
	}
	
	/*
	 * 
	 */
	@Override
	public synchronized void salirDelParque(String puerta) 
	{
		comprobarAntesDeSalir(); //check para determinar si el hilo puede continuar
		
		
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		// Se decrementa la cantidad de personas en el parque y en la puerta
		contadorPersonasTotales--;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		
		long tActual = System.currentTimeMillis();
		tmedio = (tmedio + (tActual - tInicial)) / 2.0;
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");
		
		notifyAll();  // Se notifica al resto de hilos
		
		checkInvariante();
	}
	
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	/*
	 * Comprueba que las restricciones del objeto se mantienen intactas
	 */
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert contadorPersonasTotales >= MIN;
		assert contadorPersonasTotales <= MAX;
		
	}

	protected void comprobarAntesDeEntrar()
	{
		
		while(sumarContadoresPuerta() >= MAX)
		{
			try {
				wait(); // El hilo actual se bloquea si alcanza el máximo de aforo
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println("Hilo interrumpido");
			}
		}
	}

	protected void comprobarAntesDeSalir()
	{
		while(sumarContadoresPuerta() <= MIN)
		{
			try {
				wait(); // El hilo actual se bloquea si alcanza el mínimo de aforo
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println("Hilo interrumpido");
			}
		}
	}

}
