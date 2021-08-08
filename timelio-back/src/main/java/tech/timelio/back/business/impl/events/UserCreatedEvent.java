package tech.timelio.back.business.impl.events;

public class UserCreatedEvent {
	protected MailInfos infos;
	
	public UserCreatedEvent(MailInfos infos) {
		this.infos = infos;
	}

	public MailInfos getInfos() {
		return infos;
	}

	public void setInfos(MailInfos infos) {
		this.infos = infos;
	}
}
