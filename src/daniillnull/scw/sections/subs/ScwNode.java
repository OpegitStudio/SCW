package daniillnull.scw.sections.subs;

import daniillnull.collada.ColladaIndex;
import daniillnull.collada.animation.ColladaAnimation;
import daniillnull.collada.animation.ColladaSampler;
import daniillnull.collada.instance.ColladaCameraInstance;
import daniillnull.collada.instance.ColladaControllerInstance;
import daniillnull.collada.instance.ColladaGeometryInstance;
import daniillnull.collada.instance.ColladaInstance;
import daniillnull.collada.misc.ColladaInput;
import daniillnull.collada.node.ColladaNode;
import daniillnull.collada.source.ColladaSource;
import daniillnull.collada.source.ColladaSourceAccessor;
import daniillnull.scw.ScwSerializableEntity;
import daniillnull.scw.sections.instance.ScwCameraInstance;
import daniillnull.scw.sections.instance.ScwControllerInstance;
import daniillnull.scw.sections.instance.ScwGeometryInstance;
import daniillnull.scw.sections.instance.ScwInstance;
import daniillnull.scw.util.ScwInputStream;
import daniillnull.scw.util.ScwOutputStream;
import daniillnull.scw.util.Suffix;
import daniillnull.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ScwNode implements ScwSerializableEntity {
	public static final float ANIMATION_MULTIPLIER = 30f;  // TODO: Framerate

	public final List<ScwInstance<?>> instances;
	public final List<ScwNodeFrame> frames;
	public transient final List<ScwNode> children;
	public String name, parentName;

	public ScwNode() {
		instances = new ArrayList<>();
		frames = new ArrayList<>();
		children = new ArrayList<>();
	}

	public ScwNode(String name, String parentName) {
		this();
		this.name = name;
		this.parentName = parentName;
	}

	@Override
	public void load(ScwInputStream is) throws IOException {
		instances.clear();
		frames.clear();
		children.clear();

		name = is.readUTF();
		parentName = is.readUTF();

		int instancesCount = is.readShort();
		for (int i = 0; i < instancesCount; i++) {
			String sectionType = is.readSectionName();
			ScwInstance<?> instance = ScwInstance.createByType(sectionType);
			instance.load(is);
			instances.add(instance);
		}

		int framesCount = is.readShort();
		if (framesCount > 0) {
			int presentFlags = is.readUnsignedByte();
			ScwNodeFrame lastFrame = null;
			for (int i = 0; i < framesCount; i++) {
				int currentPresentFlags = (i != 0 ? presentFlags : 127);
				ScwNodeFrame frame = new ScwNodeFrame();
				frame.load(is, currentPresentFlags, lastFrame);
				lastFrame = frame;
				frames.add(frame);
			}
		}

		//Assert.doAssert((framesCount >= 2 && instancesCount >= 1), "Non-joint node has more than 1 frames!");

		System.out.println("[INFO] Parsed node: " + name + " (" + parentName + "): instances: " + instancesCount + ", transforms: " + framesCount);
	}

	@Override
	public void save(ScwOutputStream os) throws IOException {
		os.writeUTF(name);
		os.writeUTF(parentName);

		os.writeShort(instances.size());
		for (ScwInstance<?> instance : instances) {
			os.writeSectionName(instance.getSectionName());
			instance.save(os);
		}

		os.writeShort(frames.size());
		if (!frames.isEmpty()) {
			int presentFlags = 127; // TODO: Flags calculation
			boolean isFirst = true;
			os.write(presentFlags);
			for (ScwNodeFrame frame : frames) {
				int currentPresentFlags = presentFlags;
				if (isFirst) {
					currentPresentFlags = 127;
					isFirst = false;
				}

				frame.save(os, currentPresentFlags);
			}
		}
	}

	public void importFrames(ScwNode animationNode) {
		frames.clear();
		frames.addAll(animationNode.frames);

		for (ScwNode child : children) {
			for (ScwNode aniChild : animationNode.children) {
				if (child.name.equals(aniChild.name)) {
					child.importFrames(aniChild);
				}
			}
		}
	}

	public void appendWithChildren(List<ScwNode> nodes) {
		nodes.add(this);
		for (ScwNode child : children) {
			child.appendWithChildren(nodes);
		}
	}

	public void buildIndex(Map<String, ScwNode> index) {
		Assert.doAssert(index.containsKey(name), "Node \"" + name + "\" exists already!");
		index.put(name, this);
		for (ScwNode child : children) {
			child.buildIndex(index);
		}
	}

	public void loadCollada(ColladaNode colladaNode, ColladaIndex colladaIndex) {
		frames.clear();
		instances.clear();
		children.clear();

		if (colladaNode.matrix != null) {
			ScwNodeFrame baseFrame = new ScwNodeFrame();
			baseFrame.loadMatrix(colladaNode.matrix.value);
			frames.add(baseFrame);
		}

		Assert.doAssert((colladaNode.type == ColladaNode.Type.JOINT && !colladaNode.instances.isEmpty()), "JOINT node has instances!");

		for (ColladaInstance colladaInstance : colladaNode.instances) {
			ScwInstance instance;
			if (colladaInstance instanceof ColladaGeometryInstance) {
				instance = new ScwGeometryInstance();
			} else if (colladaInstance instanceof ColladaControllerInstance) {
				instance = new ScwControllerInstance();
			} else if (colladaInstance instanceof ColladaCameraInstance) {
				instance = new ScwCameraInstance();
			} else {
				throw new RuntimeException("Unknown ColladaInstance: " + colladaInstance);
			}
			instance.loadCollada(colladaInstance, colladaIndex);
			instances.add(instance);
		}

		for (ColladaNode colladaChild : colladaNode.children) {
			ScwNode child = new ScwNode(colladaChild.id, name);
			child.loadCollada(colladaChild, colladaIndex);
			children.add(child);
		}
	}

	public void loadColladaAnimation(ColladaSampler colladaAnimationSampler, ColladaIndex colladaIndex) {
		ColladaSource inputTimeSource = null, outputTransformsSource = null, interpolationsSource = null;
		for (ColladaInput colladaInput : colladaAnimationSampler.inputs) {
			ColladaSource colladaSource = colladaIndex.get(colladaInput.source);
			switch (colladaInput.semantic) {
				case "INPUT":
					inputTimeSource = colladaSource;
					break;
				case "OUTPUT":
					outputTransformsSource = colladaSource;
					break;
				case "INTERPOLATION":
					interpolationsSource = colladaSource;
					break;
				default:
					throw new RuntimeException("Unknown input semantic in ColladaSampler: " + colladaInput.semantic);
			}
		}

		Assert.doAssert((inputTimeSource == null || outputTransformsSource == null || interpolationsSource == null), "No such ColladaSampler inputs");
		Assert.doAssert(!inputTimeSource.techniqueCommon.accessor.params.equals(ColladaSourceAccessor.TIME_FLOAT_PARAMS), "Invalid INPUT format in ColladaSampler");
		Assert.doAssert(!outputTransformsSource.techniqueCommon.accessor.params.equals(ColladaSourceAccessor.TRANSFORM_FLOAT4x4_PARAMS), "Invalid OUTPUT format in ColladaSampler");
		Assert.doAssert(!interpolationsSource.techniqueCommon.accessor.params.equals(ColladaSourceAccessor.INTERPOLATION_NAME_PARAMS), "Invalid INTERPOLATION format in ColladaSampler");

		frames.clear();

		float[] inputTime = inputTimeSource.array.getFloats();
		float[] tMatrix = outputTransformsSource.array.getFloats();
		String[] interpolations = interpolationsSource.array.getNames();
		for (int i = 0; i < inputTime.length; i++) {
			Assert.doAssert(!interpolations[i].equals("LINEAR"), "Non-linear interpolation is not supported");

			ScwNodeFrame scwNodeFrame = new ScwNodeFrame();
			float[] matrix = Arrays.copyOfRange(tMatrix, (i * 16), (i * 16) + 16);
			scwNodeFrame.id = (int) (inputTime[i] * ANIMATION_MULTIPLIER);
			scwNodeFrame.loadMatrix(matrix);
			frames.add(scwNodeFrame);
		}

		System.out.println("[INFO] Node " + name + " loaded " + frames.size() + " frames");
	}

	public ColladaNode asCollada() {
		ColladaNode colladaNode = new ColladaNode(name, name);

		if (frames.size() >= 1) {
			ScwNodeFrame firstFrame = frames.get(0);
			colladaNode.matrix = new ColladaNode.NamedFloatArray(firstFrame.matrix(), "transform");
		}

		for (ScwInstance<?> instance : instances) {
			colladaNode.instances.add(instance.asCollada());
		}
		boolean isJoint = (instances.isEmpty());
		for (ScwNode child : children) {
			ColladaNode colladaChild = child.asCollada();
			colladaNode.children.add(colladaChild);
			isJoint &= (colladaChild.type == ColladaNode.Type.JOINT);
		}
		if (isJoint) {
			colladaNode.type = ColladaNode.Type.JOINT;
		}
		return colladaNode;
	}

	public ColladaAnimation asColladaAnimation() {
		int framesCount = frames.size();
		if (framesCount >= 2) {
			float[] inputTime = new float[framesCount];
			float[] tMatrix = new float[framesCount * 16];
			String[] interpolations = new String[framesCount];
			for (int i = 0; i < framesCount; i++) {
				ScwNodeFrame frame = frames.get(i);
				float[] matrix = frame.matrix();

				System.arraycopy(matrix, 0, tMatrix, i * 16, 16);

				inputTime[i] = frame.id / ANIMATION_MULTIPLIER;
				interpolations[i] = "LINEAR";
			}

			String animationId = Suffix.create(name, Suffix.ANIMATION);

			ColladaSource inputTimeSource = new ColladaSource(animationId + "-input", inputTime, 1, ColladaSourceAccessor.TIME_FLOAT_PARAMS);
			ColladaSource outputTransformsSource = new ColladaSource(animationId + "-transform-output", tMatrix, 16, ColladaSourceAccessor.TRANSFORM_FLOAT4x4_PARAMS);
			ColladaSource interpolationsSource = new ColladaSource(animationId + "-interpolation-output", interpolations, 1, ColladaSourceAccessor.INTERPOLATION_NAME_PARAMS);

			return new ColladaAnimation(animationId, name, inputTimeSource, outputTransformsSource, interpolationsSource);
		}
		return null;
	}

	public void appendColladaAnimations(List<ColladaAnimation> animations) {
		ColladaAnimation colladaAnimation = asColladaAnimation();
		if (colladaAnimation != null) {
			animations.add(colladaAnimation);
		}
		for (ScwNode child : children) {
			child.appendColladaAnimations(animations);
		}
	}
}
