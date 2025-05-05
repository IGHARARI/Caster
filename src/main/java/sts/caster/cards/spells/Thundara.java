package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ArbitraryCardAction;
import sts.caster.actions.LightningDamageAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static sts.caster.core.CasterMod.makeCardPath;

public class Thundara extends CasterCard {

    public static final String ID = CasterMod.makeID("Thundara");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("thundara.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int DELAY_TURNS = 1;
    private static final int BASE_DAMAGE = 3;
    private static final int TOTAL_HITS = 3;

    public Thundara() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns = DELAY_TURNS;
        magicNumber = baseMagicNumber = TOTAL_HITS;
        setCardElement(MagicElement.ELECTRIC);
        upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        List<AbstractCard> inHandSpells = p.hand.group.stream()
                .filter((c) -> c != this && c.type == CasterCardType.SPELL && c.costForTurn > 0)
                .collect(Collectors.toList());
        if (inHandSpells.size() > 0) {
            AbstractCard card = inHandSpells.get(AbstractDungeon.cardRandomRng.random(inHandSpells.size() - 1));
            addToBot(new ArbitraryCardAction(card, (c) -> {
                c.flash();
                c.setCostForTurn(-99);
            }));
        }
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            for (int i = 0; i < magicNumber; i++) {
                actionsList.add(new LightningDamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage, DamageType.NORMAL), AttackEffect.SLASH_VERTICAL));
            }
            return actionsList;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            ++this.timesUpgraded;
            this.upgraded = true;
            this.initializeTitle();
        }
    }

    @Override
    public int getIntentNumber() {
        return spellDamage * magicNumber;
    }
}
