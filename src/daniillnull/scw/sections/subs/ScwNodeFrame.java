package daniillnull.scw.sections.subs;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;

import java.io.IOException;

public class ScwNodeFrame {
	public int id;
	public float rotationX, rotationY, rotationZ, rotationW;
	public float positionX, positionY, positionZ;
	public float scaleX, scaleY, scaleZ;

	public ScwNodeFrame() {
	}

	public void load(ScwInputStream is, int flags, ScwNodeFrame base) throws IOException {
		id = is.readUnsignedShort();

		if ((flags & 1) != 0) {
			rotationX = is.readFloat16();
			rotationY = is.readFloat16();
			rotationZ = is.readFloat16();
			rotationW = is.readFloat16();
		} else {
			rotationX = base.rotationX;
			rotationY = base.rotationY;
			rotationZ = base.rotationZ;
			rotationW = base.rotationW;
		}

		positionX = (((flags & 2) != 0) ? is.readFloat() : base.positionX);
		positionY = (((flags & 4) != 0) ? is.readFloat() : base.positionY);
		positionZ = (((flags & 8) != 0) ? is.readFloat() : base.positionZ);

		scaleX = (((flags & 16) != 0) ? is.readFloat() : base.scaleX);
		scaleY = (((flags & 32) != 0) ? is.readFloat() : base.scaleY);
		scaleZ = (((flags & 64) != 0) ? is.readFloat() : base.scaleZ);
	}

	public void save(ScwOutputStream os, int flags) throws IOException {
		os.writeShort(id);

		if ((flags & 1) != 0) {
			os.writeFloat16(rotationX);
			os.writeFloat16(rotationY);
			os.writeFloat16(rotationZ);
			os.writeFloat16(rotationW);
		}

		if ((flags & 2) != 0) os.writeFloat(positionX);
		if ((flags & 4) != 0) os.writeFloat(positionY);
		if ((flags & 8) != 0) os.writeFloat(positionZ);

		if ((flags & 16) != 0) os.writeFloat(scaleX);
		if ((flags & 32) != 0) os.writeFloat(scaleY);
		if ((flags & 64) != 0) os.writeFloat(scaleZ);

		// System.out.println(" scaleX=" + scaleX + ", scaleY=" + scaleY + ", scaleZ=" + scaleZ);
	}

	public void loadMatrix(float[] m) {
		Vector3 position = new Vector3();
		Quaternion quaternion = new Quaternion();
		Matrix4 matrix = new Matrix4(m);
		matrix.tra();
		matrix.getRotation(quaternion, true);
		matrix.getTranslation(position);

		// System.out.println(" angleX=" + quaternion.getAngleAround(1, 0, 0) +
		// 		", angleY=" + quaternion.getAngleAround(0, 1, 0) +
		// 		", angleZ=" + quaternion.getAngleAround(0, 0, 1));

		positionX = position.x;
		positionY = position.y;
		positionZ = position.z;

		rotationX = quaternion.x;
		rotationY = quaternion.y;
		rotationZ = quaternion.z;
		rotationW = quaternion.w;

		scaleX = matrix.getScaleX();
		scaleY = matrix.getScaleY();
		scaleZ = matrix.getScaleZ();

		// System.out.println(" scaleX=" + scaleX + ", scaleY=" + scaleY + ", scaleZ=" + scaleZ);
	}


	public float[] matrix() {
		Matrix4 matrix = new Matrix4();
		matrix.set(positionX, positionY, positionZ, rotationX, rotationY, rotationZ, rotationW, scaleX, scaleY, scaleZ);
		matrix.tra();
		return matrix.getValues();
	}
}
