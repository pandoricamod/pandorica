package io.github.pandoricamod.entity.magmator;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.MathHelper;

public class MagmatorEntityModel extends CompositeEntityModel<MagmatorEntity> {
    private final ModelPart head;
    private final ModelPart crest;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public MagmatorEntityModel() {
        textureWidth = 128;
        textureHeight = 128;

        head = new ModelPart(this, 0, 27);
        head.addCuboid(-7.0F, -9.0F, -7.0F, 16, 12, 14);
        crest = new ModelPart(this);
        crest.addCuboid(-8.0F, 0.0F, -8.0F, 18, 11, 16);
        leftLeg = new ModelPart(this, 0, 53);
        leftLeg.addCuboid(-14.0F, -13.0F, -4.0F, 8, 37, 8);
        rightLeg = new ModelPart(this, 0, 53);
        rightLeg.addCuboid(8.0F, -13.0F, -4.0F, 8, 37, 8);
    }

    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(head, crest, leftLeg, rightLeg);
    }

    public void setAngles(MagmatorEntity magmatorEntity, float f, float g, float h, float i, float j) {
        float k = 0.4F * g;
        leftLeg.pitch = MathHelper.cos(f * 0.6662F + 3.1415927F) * k;
        rightLeg.pitch = MathHelper.cos(f * 0.6662F) * k;
    }
}
