package sts.caster.cards.attacks;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.util.TextureHelper;

import java.util.ArrayList;

import static com.badlogic.gdx.graphics.Color.valueOf;
import static sts.caster.core.CasterMod.makeCardPath;
import static sts.caster.core.CasterMod.makeVFXPath;

public class Demi extends CasterCard {

    public static final String ID = CasterMod.makeID("Demi");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("demi.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE_PERCENT = 25;
    private static final int DAMAGE_PERCENT_UPG = 10;

    public Demi() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = DAMAGE_PERCENT;
        exhaust = true;
        isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Texture demicircle = TextureHelper.getTexture(makeVFXPath("demicircle.png"));
        ArrayList<Color> applicableColors = new ArrayList<Color>();

        Color lightPurp = new Color(valueOf("C65CCF"));
        Color darkPurp = new Color(valueOf("872D8F"));
        Color darkestPurp = new Color(valueOf("73227B"));
        Color lightGold = new Color(valueOf("EFED5B"));
        Color darkGold = new Color(valueOf("CFCC20"));
        applicableColors.add(lightPurp);
        applicableColors.add(darkPurp);
        applicableColors.add(darkestPurp);
        applicableColors.add(lightGold);
        applicableColors.add(darkGold);
        float scaleRatio = Math.max(m.hb.height / demicircle.getHeight(), m.hb.width / demicircle.getWidth()) * 1.2f;

        AbstractGameEffect demiVfx = new VfxBuilder(demicircle, m.hb.cX, m.hb.cY, 1.5f)
                .setScale(0.1f)
                .scale(0.1f, scaleRatio, VfxBuilder.Interpolations.CIRCLE)
                .setColor(Color.BLACK.cpy())
                .playSoundAt(0.2f, -0.3f, "ORB_DARK_CHANNEL")
                .playSoundAt(0.7f, -0.1f, "ORB_DARK_CHANNEL")
                .playSoundAt(1.2f, -0.5f, "ORB_FROST_CHANNEL")
                .emitEvery(
                        (x,y) -> {
                            float randX = MathUtils.random(-demicircle.getWidth()/2, demicircle.getWidth()/2);
                            float randY = MathUtils.random(-demicircle.getHeight()/2, demicircle.getHeight()/2);
                            float randScale = MathUtils.random(1f/8, 1f/4);
                            Color randColor = applicableColors.get(MathUtils.random(0,4));
                            AbstractGameEffect littleBall = new VfxBuilder(demicircle, m.hb.cX + randX, m.hb.cY + randY, 1f)
                                    .setScale(0.01f)
                                    .setColor(randColor)
                                    .scale(0.01f, randScale, VfxBuilder.Interpolations.CIRCLE)
                                    .fadeOut(.15f)
                                    .build();
                            littleBall.renderBehind = false;
                            return littleBall;
                        },
                        0.25f
                )
                .andThen(0.5f)
                .scale(1.2f, 0.05f, VfxBuilder.Interpolations.ELASTICIN)
                .playSoundAt(0.3f, .2f, "ORB_DARK_EVOKE")
                .playSoundAt(0.4f, .2f, "ORB_DARK_EVOKE")
                .playSoundAt(0.45f, .2f, "ORB_DARK_EVOKE")
                .build();
        demiVfx.renderBehind = true;

        addToBot(new VFXAction(demiVfx, 1.5f));
    	int hpDamage = (m.currentHealth * magicNumber) / 100;
    	addToBot(new RemoveAllBlockAction(m, p));
    	addToBot(new LoseHPAction(m, p, hpDamage));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(DAMAGE_PERCENT_UPG);
            initializeDescription();
        }
    }
}
