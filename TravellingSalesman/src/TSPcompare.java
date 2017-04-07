/* 
 *  Lab 3: Travelling Salesman Problem
 *  4-7-2017
 *  Authors: Keely Weisbeck
 *  		 Spencer Cornish
 */
import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stopwatch;

public class TSPcompare {

	private double[][] cityArr;

	public TSPcompare(int num) {
		cityArr = new double[num][3];
		for (int i = 0; i < cityArr.length; i++) // Fill cities with random data
		{
			cityArr[i][0] = (Math.random() * 100); // RandomX
			cityArr[i][1] = (Math.random() * 100); // RandomY
			cityArr[i][2] = 0; // Set visited for all cities to 0
		}
	}

	public void greedy() {
		Stopwatch timer = new Stopwatch();
		double cost = 0.;
		Queue<Integer> visitOrder = new Queue<Integer>();
		int curCity = 0; // The current city we are starting at
		while (visitOrder.size() < cityArr.length) {
			int bestCity = -1;
			double bestDist = Integer.MAX_VALUE;
			if (cityArr.length - visitOrder.size() == 1) { // Go Home case
				double dx = cityArr[0][0] - cityArr[curCity][0];
				double dy = cityArr[0][1] - cityArr[curCity][1];
				double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
				cityArr[curCity][2] = 1; // Set city we're leaving to visited
				visitOrder.enqueue(curCity); // place the city we're leaving on
												// the queue
				curCity = 0; // Set current city to the closest one wefound
				cost += dist; // Add distance to cost
				visitOrder.enqueue(0); // place the city we're leaving on the
										// queue
				break;
			}
			for (int i = 0; i < cityArr.length; i++) {
				if (i == curCity || cityArr[i][2] == 1.0)
					continue;// Checks for dupe
				// city and an
				// already visited
				// city
				else // If Unvisited and not dupe
				{
					double dx = cityArr[i][0] - cityArr[curCity][0];
					double dy = cityArr[i][1] - cityArr[curCity][1];
					double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
					if (dist < bestDist) // if current city is closer
					{
						bestCity = i; // New best city index
						bestDist = dist; // New best distance
					}
				}
			}
			// Push current city to queue, set new curCity, add to cost
			cityArr[curCity][2] = 1; // Set city we're leaving to visited
			visitOrder.enqueue(curCity); // place the city we're leaving on the
											// queue
			curCity = bestCity; // Set current city to the closest one wefound
			cost += bestDist; // Add distance to cost

		}
		System.out.println("Greedy Tour:");

		while (!visitOrder.isEmpty()) {
			System.out.print(visitOrder.dequeue()); // Prints cities in the queue order of visitation
			if (visitOrder.size() > 0) {
				System.out.print(" -> ");

			}
		}
		System.out.println("\nTotal Cost: " + cost);
		double timeElapsed = timer.elapsedTime();
		System.out.println("Time to Find: " + timeElapsed + " sec");
	}

	public void twiceAroundTree() {
		double cost = 0.0;  // Inits cost value
		Stopwatch timer = new Stopwatch(); // Used for timing
		EdgeWeightedGraph graph = new EdgeWeightedGraph(cityArr.length); // new EWG at length of number of cities
		for (int i = 0; i < cityArr.length; i++) {
			for (int j = 0; j < cityArr.length; j++) {
				double weight = Math.sqrt(Math.pow(cityArr[i][0] - cityArr[j][0], 2) + Math.pow(cityArr[i][1] - cityArr[j][1], 2)); // Calculate
																														// Distance
				Edge e = new Edge(i, j, weight); //defines each edge so they can be put in a graph
				graph.addEdge(e); // fill graph with edges
			}
		}

		EdgeWeightedDigraph digraph = new EdgeWeightedDigraph(cityArr.length, graph.E()); //creates an edge weighted digraph using mst edge and vertex values 
		DepthFirstOrder order = new DepthFirstOrder(digraph); //puts the vertices into a stack 
		
		//add the edge lengths together to get cost
		for (DirectedEdge num : digraph.edges()) {
			cost += num.weight();
		}
		
		System.out.println("Twice-Around-The-Tree Tour:");
		int iter = cityArr.length; //sets iter equal to the number of vertices 
		
		for (int num : order.reversePost()) { //Iterates through sorted and cleaned values
			System.out.print(num); 
			iter--;
			if (iter > -1) {
				System.out.print(" -> ");

			}
			//if we reach the end of the stack add path back to home
			if (iter == 0) {
				cost += Math.sqrt(Math.pow(cityArr[0][0] - cityArr[num][0], 2) + Math.pow(cityArr[0][1] - cityArr[num][1], 2));
				System.out.print("0\n"); 
			}
		}
		System.out.println("Total Cost: " + cost); //prints total cost
		double timeElapsed = timer.elapsedTime(); //calculates time
		System.out.println("Time to Find: " + timeElapsed + " sec\n"); //prints total time 
	}

	public static void main(String[] args) {
		int num = Integer.parseInt(args[0]); //allows to be called from command line 
		TSPcompare traveller = new TSPcompare(num); //makes new instance of TSP class
		traveller.twiceAroundTree(); //calls twice around tree 
		traveller.greedy(); //calls greedy 

	}
}
