package daniillnull.collada.animation;

import daniillnull.collada.ColladaBaseObject;
import daniillnull.collada.ColladaIndex;
import daniillnull.collada.misc.ColladaInput;
import daniillnull.collada.source.ColladaSource;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class ColladaAnimation extends ColladaBaseObject {
	@XmlElement(name = "source")
	public final List<ColladaSource> sources;

	@XmlElement(name = "sampler")
	public final List<ColladaSampler> samplers;

	@XmlElement(name = "channel")
	public final List<ColladaChannel> channels;

	@XmlElement(name = "animation")
	public final List<ColladaAnimation> animations;

	public ColladaAnimation() {
		sources = new ArrayList<>();
		samplers = new ArrayList<>();
		channels = new ArrayList<>();
		animations = new ArrayList<>();
	}

	public ColladaAnimation(String id, String targetBoneId, ColladaSource inputTimeSource, ColladaSource outputTransformsSource, ColladaSource interpolationSource) {
		this.id = id;
		sources = List.of(inputTimeSource, outputTransformsSource, interpolationSource);

		List<ColladaInput> samplerInputs = List.of(new ColladaInput("INPUT", inputTimeSource.id),
				new ColladaInput("OUTPUT", outputTransformsSource.id),
				new ColladaInput("INTERPOLATION", interpolationSource.id));
		ColladaSampler sampler = new ColladaSampler(id + "-sampler", samplerInputs);
		ColladaChannel channel = new ColladaChannel(sampler.id, targetBoneId + "/transform");

		samplers = List.of(sampler);
		channels = List.of(channel);

		animations = new ArrayList<>();
	}

	public List<ColladaChannel> getChannelsRecursively() {
		List<ColladaChannel> list = new ArrayList<>(channels);
		for (ColladaAnimation child : animations) {
			list.addAll(child.getChannelsRecursively());
		}
		return list;
	}

	@Override
	protected void buildIndex(ColladaIndex index) {
		super.buildIndex(index);
		for (ColladaAnimation child : animations) {
			child.buildIndex(index);
		}
		for (ColladaSampler sampler : samplers) {
			index.register(sampler.id, sampler);
		}
		for (ColladaSource source : sources) {
			index.register(source.id, source);
		}
	}
}
