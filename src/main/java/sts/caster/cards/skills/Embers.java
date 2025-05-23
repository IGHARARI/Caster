package sts.caster.cards.skills;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import sts.caster.actions.ThawCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.FrozenCardMod;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

import static sts.caster.core.CasterMod.makeCardPath;

public class Embers extends CasterCard {

    public static final String ID = CasterMod.makeID("Embers");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("embers.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = -2;
    private static final int THAW_AMOUNT = 2;
    private static final int ENERGY_AMOUNT = 1;
    private static final int UPG_ENERGY_AMOUNT = 1;


    public Embers() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = THAW_AMOUNT;
        m2 = baseM2 = ENERGY_AMOUNT;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    public void thawActions() {
        this.superFlash();
        addToBot(new VFXAction(new ShowCardBrieflyEffect(this)));
        addToBot(new WaitAction(0.5f));
        addToBot(new ThawCardAction(this));
        addToBot(new GainEnergyAction(m2));
        addToBot(new ThawCardAction(magicNumber, false, true, this));
    }

    @Override
    public void atTurnStart() {
        if (CardModifierManager.hasModifier(this, FrozenCardMod.ID)) {
            this.thawActions();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            upgradeM2(UPG_ENERGY_AMOUNT);
        }
    }
}
