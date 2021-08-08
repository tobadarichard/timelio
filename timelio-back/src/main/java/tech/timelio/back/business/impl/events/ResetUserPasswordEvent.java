package tech.timelio.back.business.impl.events;

public class ResetUserPasswordEvent {
	protected MailInfos infos;
	
	public ResetUserPasswordEvent(MailInfos infos) {
		this.infos = infos;
	}

	public MailInfos getInfos() {
		return infos;
	}

	public void setInfos(MailInfos infos) {
		this.infos = infos;
	}
}
