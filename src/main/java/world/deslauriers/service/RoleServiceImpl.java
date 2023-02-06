package world.deslauriers.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import world.deslauriers.model.database.Role;
import world.deslauriers.model.database.User;
import world.deslauriers.model.database.UserRole;
import world.deslauriers.repository.RoleRepository;
import world.deslauriers.repository.UserRoleRepository;

import java.util.HashSet;

@Singleton
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public RoleServiceImpl(RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Mono<Role> getRole(String role){
        return roleRepository.findByRole(role);
    }

    @Override
    public Mono<Role> getById(Long id){
        return roleRepository.findById(id);
    }

    @Override
    public Flux<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    @Override
    public Mono<Role> save(Role role){
        return roleRepository.save(new Role(role.role().toUpperCase(), role.title(), role.description()));
    }

    @Override
    public Mono<Role> update(Role role){
        return roleRepository.update(new Role(role.id(), role.role().toUpperCase(), role.title(), role.description()));
    }

    @Override
    public void resolveRoles(HashSet<Role> roles, User user){

        roles.forEach(role -> {
            if (user.userRoles().stream().noneMatch(userRole -> userRole.role().id().equals(role.id()))){
                roleRepository.findById(role.id())
                        .flatMap(r -> userRoleRepository.save(new UserRole(user, r)))
                        .switchIfEmpty(Mono.defer(() -> {
                            log.error("Attempt to add role that does not exist.");
                            return Mono.empty();
                        }))
                        .subscribe(userRole -> log.info("Created xref id: {}, user: {} <--> role: {}",
                                userRole.id(), userRole.user().username(), userRole.role().title()));
            }
        });

    }
}
