package sts.caster.cards.spells;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import sts.caster.actions.ActionOnAllEnemiesAction;
import sts.caster.actions.DelayedDamageAllEnemiesAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.RecurringSpellCardMod;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;
import java.util.List;

import static sts.caster.core.CasterMod.makeCardPath;

public class Tundra extends CasterCard {

    public static final String ID = CasterMod.makeID("Tundra");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("tundra.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_RECUR = 2;
    private static final int BASE_VULN = 1;
    private static final int BASE_DAMAGE = 6;
    private static final int UPGR_DAMAGE = 4;


    public Tundra() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseMagicNumber = magicNumber = BASE_VULN;
        exhaust = true;
        setCardElement(MagicElement.ICE);
    }

    @Override
    protected List<AbstractCardModifier> getInitialModifiers() {
        List<AbstractCardModifier> mods = new ArrayList<>();
        mods.add(new RecurringSpellCardMod(BASE_RECUR));
        return mods;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ActionOnAllEnemiesAction(
                mon -> new ApplyPowerAction(mon, AbstractDungeon.player, new VulnerablePower(mon, magicNumber, false), magicNumber)
        ));
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            actionsList.add(new ActionOnAllEnemiesAction(
                    m -> new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, c.magicNumber, false), c.magicNumber)
            ));
            actionsList.add(new DelayedDamageAllEnemiesAction(AbstractDungeon.player, c.spellDamage, c.cardElement, AttackEffect.SMASH));
//            actionsList.add(new QueueDelayedCardAction(c, BASE_DELAY, t));
            return actionsList;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeSpellDamage(UPGR_DAMAGE);
        }
    }
}
