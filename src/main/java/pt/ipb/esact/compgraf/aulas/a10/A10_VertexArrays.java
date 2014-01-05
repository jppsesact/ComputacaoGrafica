package pt.ipb.esact.compgraf.aulas.a10;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import pt.ipb.esact.compgraf.tools.Camera;
import pt.ipb.esact.compgraf.tools.Cameras;
import pt.ipb.esact.compgraf.tools.DefaultGLWindow;

import com.jogamp.common.nio.Buffers;

public class A10_VertexArrays extends DefaultGLWindow {

	// Array com a posição da luz
	float[] positionLitght0 = { 0.0f, 3.0f, 7.0f, 1.0f };
	private FloatBuffer vertices;
	private IntBuffer indices;
	private FloatBuffer colors;
	
	public A10_VertexArrays() {
		super("A10 Vertex Arrays", true);
		
		setMousePan(true);
		setMouseZoom(true);
		
		Camera camera = new Camera();
		camera.eye.x = 0.0f;
		camera.eye.y = 0.0f;
		camera.eye.z = 3.0f;
		
		camera.at.y = 0.0f;
		
		Cameras.setCurrent(camera);
	}
	
	@Override
	public void init() {
		// Definir a cor de background (RGBA={0, 0, 0, 255})
		glClearColor(0.0f, 0.0f, 0.0f, 1f);
		
		// Activar o teste de profundidade
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_CULL_FACE);

		glCullFace(GL_BACK);

		// Configurar as luzes
		configureLighting();
		
		// Configurar os materiais
		configureMaterials();
		
		// Inicializar os Vertex Arrays
		configureVertexArrays();
	}
	
	private void configureMaterials() {
		// Configurar Color Tracking
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
		glMateriali(GL_FRONT, GL_SHININESS, 100);
		
		// Especularidade do material definida explicitamente
		float[] specRef = {1.0f, 1.0f, 1.0f, 1.0f};
		glMaterialfv(GL_FRONT, GL_SPECULAR, specRef, 0);
	}

	private void configureLighting() {
		// Ativar a Lighting globalmente
		glEnable(GL_LIGHTING);
		
		// Definição do Modelo de luz para a luz ambiente
		float[] ambientLowLight = { 0.1f, 0.1f, 0.1f, 1.0f };
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambientLowLight, 0);

		// Este é o array com o RGB da luz ambiente
		float[] ambientLight = { 0.2f, 0.2f, 0.2f, 1.0f };
		// Este é o array com o RGB da luz difusa
		float[] diffuseLight = { 0.4f, 0.4f, 0.4f, 1.0f };
		// Este é o array com o RGB da luz especular
		float[] specularLight = { 0.5f, 0.5f, 0.5f, 1.0f };

		// Configurar e Activar a Luz 0
		glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);		// Componente ambiente
		glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuseLight, 0);		// Componente difusa
		glLightfv(GL_LIGHT0, GL_SPECULAR, specularLight, 0);		// Componente especular

		// Activação da luz 0
		glEnable(GL_LIGHT0);
	}

	@Override
	public void release() {
		// Libertar as texturas (GPU)z
	}
	
	public void configureVertexArrays() {
		// Preparar os vertices
		vertices = Buffers.newDirectFloatBuffer(new float[] {
			// Face frontal
			 1.0f, -1.0f,  1.0f, // front-bottom-right - index: 0 
			 1.0f,  1.0f,  1.0f, // front-top-right    - index: 1
			-1.0f,  1.0f,  1.0f, // front-top-left     - index: 2
			-1.0f, -1.0f,  1.0f, // front-bottom-left  - index: 3

			 1.0f, -1.0f, -1.0f, // back-bottom-right  - index: 4
			 1.0f,  1.0f, -1.0f, // back-top-right     - index: 5
			-1.0f,  1.0f, -1.0f, // back-top-left      - index: 6
			-1.0f, -1.0f, -1.0f, // back-bottom-left   - index: 7
		});
		
		// Indices que ligam os vertices com as primitivas
		// ATENCAO: Winding interessa
		indices = Buffers.newDirectIntBuffer(new int[] {
			0, 1, 2, 3, // front
			7, 6, 5, 4, // back
			0, 4, 5, 1, // right
			3, 2, 6, 7, // left
			3, 7, 4, 0, // bottom
			1, 5, 6, 2  // top
		});
		
		// Cores dos vertices
		colors = Buffers.newDirectFloatBuffer(new float[] {
			1.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			1.0f, 1.0f, 1.0f,

			1.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
		});
	}
	
	@Override
	public void render(int width, int height) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		// Reposicionar a luz
		glLightfv(GL_LIGHT0, GL_POSITION, positionLitght0, 0);
		glColor3f(1.0f, 1.0f, 1.0f);
		
		glPushClientAttrib(GL_CLIENT_VERTEX_ARRAY_BIT);
			// Ativar o client state GL_VERTEX_ARRAY
			glEnableClientState(GL_VERTEX_ARRAY);
			// Dizer ao GPU qual o array com os vertices
			glVertexPointer(3, GL_FLOAT, 0, vertices);

			// EXTRA: Ativar o client state GL_COLOR_ARRAY
			glEnableClientState(GL_COLOR_ARRAY);
			// Dizer ao GPU qual o array com as colors
			glColorPointer(3, GL_FLOAT, 0, colors);
		
			// Desenhar o cubo como QUADS
			glDrawElements(GL_QUADS, indices.capacity(), GL_UNSIGNED_INT, indices);
			
			// Desenhar o cubo como POINTS
			glPointSize(20.0f);
			glDrawElements(GL_POINTS, indices.capacity(), GL_UNSIGNED_INT, indices);
		glPopClientAttrib();
		
		/**
		 * Alterar o cubo... mover a face frontal no z usando as teclas +/-
		 */
		if(isKeyPressed('+') || isKeyPressed('-')) {
			float increment = 1.0f;
			// se é '-' inverter o incremento
			if(isKeyPressed('-'))
				increment *= -1.0f;
			
			// Mover a coordenada z dos primeiros 4 vertices
			vertices.put(2,  vertices.get(2) + increment * timeElapsed());
			vertices.put(5,  vertices.get(2) + increment * timeElapsed());
			vertices.put(8,  vertices.get(2) + increment * timeElapsed());
			vertices.put(11, vertices.get(2) + increment * timeElapsed());
		}
	}

	
	@Override
	public void resize(int width, int height) {
		setProjectionPerspective(width, height, 100.0f, 0.001f, 500.0f);
		setupCamera();
	}

	// Função main confere capacidade de executável ao .java atual
	public static void main(String[] args) {
		new A10_VertexArrays();
	}

}