package sts.caster.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sts.caster.cards.attacks.DefaultRareAttack;
import sts.caster.core.CasterMod;
import sts.caster.util.TextureLoader;

public class RarePower extends AbstractPower {
    public AbstractCreature source;

    public static final String POWER_ID = CasterMod.makeID("RarePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("theDefaultResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theDefaultResources/images/powers/placeholder_power32.png");

    public RarePower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() { // At the start of your turn
        AbstractCard playCard = new DefaultRareAttack(); // Declare Card - the DefaultRareAttack card
        AbstractMonster targetMonster = AbstractDungeon.getRandomMonster(); // Declare Target - Random Monster

        playCard.freeToPlayOnce = true; //Self Explanatory

        if (playCard.type != AbstractCard.CardType.POWER) {
            playCard.purgeOnUse = true;
        }
        // Remove completely on use (Not Exhaust). A note - you don't need the '{}' in this if statement,
        // as it's just 1 line directly under. You can remove them, if you want.

        AbstractDungeon.actionManager.addToBottom(new QueueCardAction(playCard, targetMonster)); // Play the card on the target.
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

}
