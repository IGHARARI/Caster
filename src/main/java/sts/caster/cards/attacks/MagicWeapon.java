package sts.caster.cards.attacks;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.actions.ConditionalDiscardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.patches.spellCardType.CasterCardType;

public class MagicWeapon extends CasterCard {

    public static final String ID = CasterMod.makeID("MagicWeapon");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Attack.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int CARD_DRAW = 3;
    private static final int UPG_CARD_DRAW = 2;

    public MagicWeapon() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = CARD_DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	ArrayList<AbstractCard> cardsBeforeDraw = new ArrayList<AbstractCard>(AbstractDungeon.player.hand.group);
    	AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
    	Predicate<AbstractCard> discardPredicate = (c) ->  (!cardsBeforeDraw.contains(c) && !(c.type == CasterCardType.SPELL));
    	AbstractDungeon.actionManager.addToBottom(new ConditionalDiscardAction(discardPredicate));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPG_CARD_DRAW);
            initializeDescription();
        }
    }
}
