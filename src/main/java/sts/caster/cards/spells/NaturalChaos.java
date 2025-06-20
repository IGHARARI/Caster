package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ActionOnAllEnemiesAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.powers.MiredPower;

import java.util.ArrayList;
import java.util.UUID;

import static sts.caster.core.CasterMod.makeCardPath;

public class NaturalChaos extends CasterCard {

    public static final String ID = CasterMod.makeID("NaturalChaos");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("naturalchaos.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int UPG_COST = 0;
    private static final int BASE_DELAY = 1;
    private static final int BASE_MIRE = 1;
    private static final int UPG_MIRE = 1;


    public NaturalChaos() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        magicNumber = baseMagicNumber = BASE_MIRE;
        setCardElement(MagicElement.EARTH);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new QueueDelayedCardAction(this, delayTurns, null));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent, UUID originalUUID) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
            actions.add(new ActionOnAllEnemiesAction(monster -> new ApplyPowerAction(monster, AbstractDungeon.player, new MiredPower(monster, AbstractDungeon.player, c.magicNumber), c.magicNumber)));
            return actions;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
//            upgradeBaseCost(UPG_COST);
            upgradeMagicNumber(UPG_MIRE);
        }
    }
}
