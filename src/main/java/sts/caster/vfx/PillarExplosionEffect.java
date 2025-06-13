package sts.caster.vfx;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

import java.util.function.BiFunction;
import java.util.function.Function;

import static sts.caster.core.CasterMod.makeVFXPath;

public class PillarExplosionEffect {
    public static AbstractGameEffect buildPillarExplosion(AbstractCreature m) {
        Texture magicCircle = TextureHelper.getTexture(makeVFXPath("squish_magic.png"));
        Texture beamTexture = TextureHelper.getTexture(makeVFXPath("pillar.png"));


        CasterMod.logger.info("Starting to build pillar explosion");
        float baseX = m.hb.cX;
        float baseScale = Math.max(
                m.hb.width / magicCircle.getWidth(),
                m.hb.height / magicCircle.getHeight()
        ) * 1.5f;

        // Position the base circle slightly below the enemy’s center
        float baseY = m.hb.cY - (m.hb.height / 2.5f) * Settings.scale;



        // Config
        int pillarCount = 4;
        float pillarSpacing = magicCircle.getHeight() * baseScale * 0.9f * Settings.scale;
        float pillarInterval = 0.15f;
        float pillarStartTime = 0.4f;

        // === Helper: create a pillar circle effect ===
        BiFunction<Float, Integer, AbstractGameEffect> createPillarCircle = (x, index) -> {
            float y = baseY + (index + 1) * pillarSpacing;
            CasterMod.logger.info("Create pillar circle index " + index + " at Y:" + y);
            return new VfxBuilder(magicCircle, x, y, 0.5f)
                    .setScale(0f)
                    .fadeIn(0.1f)
                    .scale(0f, baseScale * 0.7f, VfxBuilder.Interpolations.ELASTICOUT)
                    .playSoundAt(0.0f, 1.2f + index * 0.05f, "TINGSHA")
                    .playSoundAt(0.05f, 1.0f + index * 0.03f, "STANCE_ENTER_CALM")
                    .andThen(0.4f)
                    .fadeOut(0.3f)
                    .build();
        };

        // === Helper: create the beam effect ===
        Function<Float, AbstractGameEffect> createBeamBlast = (x) -> {
            float beamHeight = pillarCount * pillarSpacing + 100f * Settings.scale;  // Stretch beyond final circle
            float beamY = baseY + beamHeight / 2f;  // Centered on the entire beam height

            CasterMod.logger.info("Create Beam blast at Y:" + beamY + " to reach down to baseY " + baseY);

            AbstractGameEffect beam = new VfxBuilder(beamTexture, x, beamY, 0.2f) // slightly longer duration
                    .setScale(0.1f)
                    .fadeIn(0.2f)
                    .useAdditiveBlending()
                    .scale(0.1f, beamHeight / beamTexture.getHeight(), VfxBuilder.Interpolations.CIRCLEOUT)
                    .playSoundAt(0.0f, 1.0f, "STANCE_ENTER_DIVINITY")
                    .playSoundAt(0.1f, 0.95f, "ATTACK_MAGIC_BEAM_SHORT")
                    .andThen(0.9f) // stay at full size
                    .andThen(0.3f) // retract
                    .scale(beamHeight / beamTexture.getHeight(), 0.2f, VfxBuilder.Interpolations.CIRCLEIN)
                    .fadeOut(0.3f)
                    .build();
            beam.renderBehind = false;
            return beam;
        };

        // === Main VfxBuilder sequence ===
        VfxBuilder builder = new VfxBuilder(magicCircle, baseX, baseY, 0.25f)
                .setScale(0f)
                .fadeIn(0.3f)
                .scale(0f, baseScale, VfxBuilder.Interpolations.ELASTICOUT)
                .playSoundAt(0.0f, 1f , "TINGSHA")
                .playSoundAt(0.05f, 0.8f, "STANCE_ENTER_CALM")
                .andThen(1.8f);

        // === Trigger pillar circles ===
        for (int i = 0; i < pillarCount; i++) {
            float delay = pillarStartTime + i * pillarInterval;
            int index = i;
            CasterMod.logger.info("Create circle in loop wtih delay" + delay + "...");
            builder = builder.triggerVfxAt(delay, 1, (x, y) -> {
                return createPillarCircle.apply(x, index);
            });
        }

        CasterMod.logger.info("Add blast with offset " + (pillarStartTime + pillarCount * pillarInterval) + "...");
        // === Trigger beam blast ===
        builder = builder.triggerVfxAt(
                pillarStartTime + pillarCount * pillarInterval,
                1,
                (x, y) -> {
                    return createBeamBlast.apply(x);
                }
        )
        .andThen(0.3f)
        .fadeOut(0.3f);

        // === Final effect ===
        CasterMod.logger.info("Build the entire thing");
        AbstractGameEffect entireExplosion = builder.build();
        entireExplosion.renderBehind = true;
        return entireExplosion;
    }
}
