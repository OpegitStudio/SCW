package daniillnull.scw.sections;

import com.badlogic.gdx.math.Quaternion;
import daniillnull.collada.ColladaIndex;
import daniillnull.collada.animation.ColladaAnimation;
import daniillnull.collada.node.ColladaNode;
import daniillnull.collada.node.ColladaVisualScene;
import daniillnull.scw.ScwSection;
import daniillnull.scw.sections.subs.ScwNode;
import daniillnull.scw.sections.subs.ScwNodeFrame;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;
import daniillnull.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ScwScene extends ScwSection {
	public final List<ScwNode> rootNodes;

	public ScwScene() {
		rootNodes = new ArrayList<>();
	}

	@Override
	public void load(ScwInputStream is) throws IOException {
		rootNodes.clear();

		HashMap<String, ScwNode> allNodes = new HashMap<>();

		int count = is.readShort();
		for (int i = 0; i < count; i++) {
			ScwNode scwNode = new ScwNode();
			scwNode.load(is);

			allNodes.put(scwNode.name, scwNode);
			if (scwNode.parentName.isEmpty()) {
				rootNodes.add(scwNode);
			} else {
				ScwNode parentNode = allNodes.get(scwNode.parentName);
				parentNode.children.add(scwNode);
			}
		}
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		List<ScwNode> nodes = new ArrayList<>();
		for (ScwNode rootNode : rootNodes) {
			rootNode.appendWithChildren(nodes);
		}

		os.writeShort(nodes.size());
		for (ScwNode node : nodes) {
			node.save(os);
		}
	}

	@Override
	public String getSectionName() {
		return "NODE";
	}

	public Map<String, ScwNode> buildIndex() {
		Map<String, ScwNode> index = new HashMap<>();
		for (ScwNode rootNode : rootNodes) {
			rootNode.buildIndex(index);
		}
		return index;
	}

	public void transformRoots(Consumer<ScwNodeFrame> frameConsumer) {
		for (ScwNode rootNode : rootNodes) {
			int sz = rootNode.frames.size();
			if (sz >= 1) {
				Assert.doAssert((sz != 1), "root node has more than 1 frames");
				ScwNodeFrame frame = rootNode.frames.get(0);
				frameConsumer.accept(frame);
			}
		}
	}

	public void rotateRoot(float yaw, float pitch, float roll) {
		Quaternion qu = new Quaternion();
		qu.setEulerAngles(yaw, pitch, roll);
		transformRoots((frame) -> {
			frame.rotationX = qu.x;
			frame.rotationY = qu.y;
			frame.rotationZ = qu.z;
			frame.rotationW = qu.w;
		});
	}

	public void scaleRoot(float x, float y, float z) {
		transformRoots((frame) -> {
			frame.scaleX *= x;
			frame.scaleY *= y;
			frame.scaleZ *= z;
		});
	}

	public void translateRoot(float x, float y, float z) {
		transformRoots((frame) -> {
			frame.positionX = x;
			frame.positionY = y;
			frame.positionZ = z;
		});
	}

	public void importFrames(ScwScene animationScene) {
		for (ScwNode rootNode : rootNodes) {
			for (ScwNode aniRootNode : animationScene.rootNodes) {
				if (rootNode.name.equals(aniRootNode.name)) {
					rootNode.importFrames(aniRootNode);
				}
			}
		}
	}

	public void loadCollada(ColladaVisualScene colladaVisualScene, ColladaIndex colladaIndex) {
		rootNodes.clear();

		for (ColladaNode colladaNode : colladaVisualScene.nodes) {
			ScwNode node = new ScwNode(colladaNode.id, "");
			node.loadCollada(colladaNode, colladaIndex);
			rootNodes.add(node);
		}
	}

	public ColladaVisualScene asCollada() {
		ColladaVisualScene colladaScene = new ColladaVisualScene("main_scene");
		for (ScwNode rootNode : rootNodes) {
			colladaScene.nodes.add(rootNode.asCollada());
		}
		return colladaScene;
	}

	public List<ColladaAnimation> asColladaAnimations() {
		List<ColladaAnimation> animations = new ArrayList<>();
		for (ScwNode rootNode : rootNodes) {
			rootNode.appendColladaAnimations(animations);
		}
		return animations;
	}
}
