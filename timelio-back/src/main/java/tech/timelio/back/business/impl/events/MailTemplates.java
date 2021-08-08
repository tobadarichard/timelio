package tech.timelio.back.business.impl.events;

public class MailTemplates {
	protected String verifierCompte;
	protected String resetMdp;
	
	public String getVerifierCompte() {
		return verifierCompte;
	}
	public void setVerifierCompte(String verifierCompte) {
		this.verifierCompte = verifierCompte;
	}
	public String getResetMdp() {
		return resetMdp;
	}
	public void setResetMdp(String resetMdp) {
		this.resetMdp = resetMdp;
	}
}
