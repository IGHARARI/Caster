package sts.caster.cards.attacks;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.util.TextureHelper;

import java.util.HashSet;

import static sts.caster.core.CasterMod.makeCardPath;
import static sts.caster.core.CasterMod.makeVFXPath;

public class GateOfBabylon extends CasterCard {

    public static final String ID = CasterMod.makeID("GateOfBabylon");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("babylon.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_DAMAGE = 4;
    private static final int UPGR_DAMAGE = 2;
    private boolean descriptionChanged;

    public GateOfBabylon() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = BASE_DAMAGE;
        baseMagicNumber = magicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	int attacks = countSpellsInMasterDeck();

        Texture magicCircle = TextureHelper.getTexture(makeVFXPath("magiccircle.png"));
        Texture sword = TextureHelper.getTexture(makeVFXPath("sword.png"));

        AbstractGameEffect gateVfx = new VfxBuilder(magicCircle, p.dialogX, p.dialogY, 1.8f)
                .fadeIn(.5f)
                .setScale(1f)
                .rotate(100f)
                .playSoundAt(0.2f, -.7f, "ORB_SLOT_GAIN")
                .emitEvery(
                        (x,y) -> {
                            float randX = MathUtils.random(-magicCircle.getWidth()/4, magicCircle.getWidth()/2);
                            float randY = MathUtils.random(-magicCircle.getHeight()/2, magicCircle.getHeight()/2);
                            return new VfxBuilder(sword, p.dialogX + randX, p.dialogY + randY, .5f)
                                    .setScale(.5f)
                                    .setAlpha(0)
                                    .fadeIn(.5f)
                                    .setScale(1f)
                                    .andThen(.33f)
                                    .moveX(p.dialogX + randX, p.dialogX + randX + Settings.WIDTH, VfxBuilder.Interpolations.LINEAR)
                                    .build();
                        },
                        1f/attacks*2
                )
                .build();


        addToBot(new VFXAction(gateVfx, 1.3f));
        for(int i = 0; i < attacks; ++i) {
            this.addToBot(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGR_DAMAGE);
            initializeDescription();
        }
    }
    
    @Override
    public void atTurnStart() {
    	updateDescription();
    }
    
    private void updateDescription() {
    	if (!descriptionChanged) {
    		rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
    		initializeDescription();
    	}
    	descriptionChanged = true;
	}

	@Override
    public void applyPowers() {
		updateDescription();
    	super.applyPowers();
    	magicNumber = countSpellsInMasterDeck();
    	isMagicNumberModified = true;
    }
    
    public static int countSpellsInMasterDeck() {
    	HashSet<String> spellIDs = new HashSet<String>();
    	for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
    		if (c.type == CasterCardType.SPELL) spellIDs.add(c.cardID);
    	}
    	return spellIDs.size();
    }
}
