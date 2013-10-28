package pt.ipb.esact.compgraf.tools.math;

import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

/**
 * A classe VectorList facilita o cálculo da normal de 3 vectores, ou do centróide
 * de um determinado polígono
 */
@SuppressWarnings("serial")
public class VectorList extends ArrayList<Vector> {

	/**
	 * Adiciona o {@link Vector} à lista atual
	 * @return A lista atual
	 */
	public VectorList append(Vector vector) {
		add(vector);
		return this;
	}
	
	/**
	 * @return O vector normal dos 3 primeiros vectores da lista actual
	 */
	Vector normalVector() {
		if(size() < 3)
			return Vector.ORIGIN;

		Vector v1 = get(0);
		Vector v2 = get(1);
		Vector v3 = get(2);
		Vector vv1 = v2.sub(v1);
		Vector vv2 = v3.sub(v1);
		Vector n = vv1.cross(vv2);
		n.normalize();
		return n;
	}

	/** 
	 * O ponto central do Triângulo definido pelos primeiros 3 Vector da lista
	 * 
	 * @return O centróide do polígono representado pelos 3 primeiros vectores da lista actual
	 */
	public Vector centroid() {
		if(size() < 3)
			return Vector.ORIGIN;

		Vector v1 = get(0);
		Vector v2 = get(1);
		Vector v3 = get(2);

		float xx = v1.x + v2.x + v3.x; xx /= 3.0;
		float yy = v1.y + v2.y + v3.y; yy /= 3.0;
		float zz = v1.z + v2.z + v3.z; zz /= 3.0;

		Vector c = new Vector(xx, yy, zz);
		return c;
	}	


	/**
	 * Desenha os vertices na lista actual
	 * 
	 * @param primitive É desenhada a lista de vectores com esta primitiva
	 */
	public void paintVertex(int primitive) {
		paintVertex(primitive, true, false);
	}
	
	/**
	 * Desenha os vertices na lista actual
	 * 
	 * @param primitive É desenhada a lista de vectores com esta primitiva
	 * @param normals Quando @c TRUE, são calculadas e definidas as normais
	 */
	public void paintVertex(int primitive, boolean normals) {
		paintVertex(primitive, normals, false);
	}
	
	/**
	 * Desenha os vertices na lista actual
	 * 
	 * @param primitive É desenhada a lista de vectores com esta primitiva
	 * @param normals Quando @c TRUE, são calculadas e definidas as normais
	 * @param paintNormals Quando @c TRUE, são representadas as normais com uma linha
	 */
	public void paintVertex(int primitive, boolean normals, boolean paintNormals) {
		GL2 gl = gl();
		
		if(normals) {
			Vector n = normalVector();
			if(n.isOrigin())
				gl.glNormal3f(n.x, n.y, n.z);
			if(paintNormals) {
				n.scale(30.0f);
				n.paint(centroid(), Color.BLUE);
			}
		}
		
		gl.glBegin(primitive);
		for(int i=0; i<size(); i++) {
			Vector v = get(i);
			gl.glVertex3f(v.x, v.y, v.z);
		}
		gl.glEnd();

		if(paintNormals) {
			gl.glPushAttrib(GL2.GL_CURRENT_BIT);
				gl.glColor3f(1.0f, 0.0f, 0.0f);
				gl.glBegin(GL2.GL_LINE_STRIP);
				for(int i=0; i<size(); i++) {
					Vector v = get(i);
					gl.glVertex3f(v.x, v.y, v.z);
				}
				gl.glEnd();
			gl.glPopAttrib();
		}
	}

	/**
	 * Define as normais do polígono representados pelos 3 vectores
	 * 
	 * @param one Um vector
	 * @param two Outro vector
	 * @param three Outro vector
	 */
	public static void glNormal(Vector one, Vector two, Vector three) {
		VectorList list = new VectorList();
		list.append(one);
		list.append(two);
		list.append(three);

		GL2 gl = gl();
		Vector n = list.normalVector();
		gl.glNormal3f(n.x, n.y, n.z);
	}

	private static GL2 gl() {
		return GLContext.getCurrentGL().getGL2();
	}

	public Vector append(float x, float y, float z) {
		Vector v = new Vector(x, y, z);
		append(v);
		return v;
	}
	
}