﻿using Mixter.Domain.Identity.Events;
using Mixter.Infrastructure;

namespace Mixter.Domain.Identity
{
    public class SessionHandler : 
        IEventHandler<UserConnected>,
        IEventHandler<UserDisconnected>
    {
        private readonly ISessionsRepository _repository;

        public SessionHandler(ISessionsRepository repository)
        {
            _repository = repository;
        }

        public void Handle(UserConnected evt)
        {
            _repository.Save(new SessionProjection(evt));
        }

        public void Handle(UserDisconnected evt)
        {
            _repository.ReplaceBy(new SessionProjection(evt));
        }
    }
}