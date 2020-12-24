package sts.caster.powers;

import static sts.caster.core.CasterMod.makePowerPath;

import java.lang.reflect.Field;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sts.caster.core.CasterMod;
import sts.caster.util.TextureHelper;

//Gain 1 dex for the turn for each card played.

public class IllusionPower extends AbstractPower {
	public AbstractCreature source;

	public static final String POWER_ID = CasterMod.makeID("Illusion");
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static final Texture tex84 = TextureHelper.getTexture(makePowerPath("illusion84.png"));
	private static final Texture tex32 = TextureHelper.getTexture(makePowerPath("illusion32.png"));
	
    private byte moveByte;
    private AbstractMonster.Intent moveIntent;
    private EnemyMoveInfo move;

	public IllusionPower(final AbstractCreature target) {
		name = NAME;
		ID = POWER_ID;
		this.owner = target;
		this.amount = -1;
		this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

		isTurnBased = false;
		canGoNegative = false;
		type = PowerType.DEBUFF;
		updateDescription();
	}
    
    @Override
    public void stackPower(int stackAmount) {}

    @Override
    public void onInitialApplication()
    {
        // Dumb action to delay grabbing monster's intent until after it's actually set-- Thanks kio :D
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                if (owner instanceof AbstractMonster) {
                    moveByte = ((AbstractMonster) owner).nextMove;
                    moveIntent = ((AbstractMonster) owner).intent;
                    try {
                        Field f = AbstractMonster.class.getDeclaredField("move");
                        f.setAccessible(true);
                        move = (EnemyMoveInfo) f.get(owner);
                        move.intent = AbstractMonster.Intent.DEFEND;
                        ((AbstractMonster) owner).createIntent();
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
                isDone = true;
            }
        });
    }

    @Override
    public void onRemove()
    {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)owner;
            if (move != null) {
                m.setMove(moveByte, moveIntent, move.baseDamage, move.multiplier, move.isMultiDamage);
            } else {
                m.setMove(moveByte, moveIntent);
            }
            m.createIntent();
            m.applyPowers();
        }
    }
	
	
	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

}
