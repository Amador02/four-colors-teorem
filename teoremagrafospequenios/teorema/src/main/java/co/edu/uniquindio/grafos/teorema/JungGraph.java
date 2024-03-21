package co.edu.uniquindio.grafos.teorema;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class JungGraph {
	public static void main(String[] args) {
		// Crear un grafo
		Graph<String, Integer> graph = new SparseGraph<>();

		// Agregar vértices (nodos) al grafo
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addVertex("E");

		// Agregar aristas (conexiones) entre los vértices
		graph.addEdge(1, "A", "B");
		graph.addEdge(2, "A", "C");
		graph.addEdge(3, "B", "C");
		graph.addEdge(4, "C", "D");
		graph.addEdge(5, "D", "E");
		graph.addEdge(6, "A", "E");
		graph.addEdge(7, "A", "D");

		// Realizar la coloración de los nodos
		Map<String, Integer> colors = colorGraph(graph);

		// Configurar el visualizador
		FRLayout<String, Integer> layout = new FRLayout<>(graph);
		layout.setSize(new Dimension(300, 300));
		VisualizationViewer<String, Integer> vv = new VisualizationViewer<>(layout);
		vv.setPreferredSize(new Dimension(350, 350));

		Color[] colores = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW };

		// Configurar el color de los nodos
		Function<String, Paint> vertexPaint = v -> {
			return colores[colors.get(v)];

		};
		vv.getRenderContext().setVertexFillPaintTransformer(input -> vertexPaint.apply(input));

		// Mostrar el visualizador en un JFrame
		JFrame frame = new JFrame("Teorema de los 4 colores");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}

	// Función para colorear los nodos del grafo de manera que los nodos adyacentes
	// no tengan el mismo color
	private static Map<String, Integer> colorGraph(Graph<String, Integer> graph) {
		Map<String, Integer> colors = new HashMap<>();
		Set<String> visited = new HashSet<>();
		for (String vertex : graph.getVertices()) {
			Set<Integer> neighborColors = new HashSet<>();
			for (String neighbor : graph.getNeighbors(vertex)) {
				if (visited.contains(neighbor)) {
					neighborColors.add(colors.get(neighbor));
				}
			}
			int color = 0;
			while (neighborColors.contains(color)) {
				color++;
			}
			colors.put(vertex, color);
			visited.add(vertex);
		}
		return colors;
	}
}
