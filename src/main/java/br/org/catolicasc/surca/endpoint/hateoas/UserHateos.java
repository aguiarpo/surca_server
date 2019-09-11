package br.org.catolicasc.surca.endpoint.hateoas;

import br.org.catolicasc.surca.endpoint.UserEndpoint;
import br.org.catolicasc.surca.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class UserHateos {

    public static void createLinkById(User user, PagedResourcesAssembler assembler){
        Link ctrlBldr = linkTo(methodOn(UserEndpoint.class).getUserById(user.getCode(), assembler)).withSelfRel();
        user.add(ctrlBldr);
    }

    public static void createLink(Optional<User> user, PagedResourcesAssembler assembler){
        Link ctrlBldr = linkTo(methodOn(UserEndpoint.class).listAll(PageRequest.of(0, 10),
                assembler))
                .withRel("users");
        user.get().add(ctrlBldr);
    }

    public static void createLink(User user, PagedResourcesAssembler assembler){
        Link ctrlBldr = linkTo(methodOn(UserEndpoint.class).listAll(PageRequest.of(0, 10),
                assembler))
                .withRel("users");
        user.add(ctrlBldr);
    }

    public static void createLink(User user, Authentication auth){
        Link ctrlBldr = linkTo(methodOn(UserEndpoint.class).getUserEmailAuth(auth)).withSelfRel();
        user.add(ctrlBldr);
    }

    public static void createLinkFindBy(User user, PagedResourcesAssembler assembler){
        user.add(createLinkFindByName(assembler));
        user.add(createLinkFindByNameLike(assembler));
        user.add(createLinkFindByEmail(assembler));
        user.add(createLinkFindByLevelsOfAccess(assembler));
        user.add(createLinkFindByAll(assembler));
    }

    public static PagedResources<User> createLinkFindBy(Page<User> users, PagedResourcesAssembler assembler){
        PagedResources<User> resources = assembler.toResource(users);
        resources.add(createLinkFindByName(assembler));
        resources.add(createLinkFindByNameLike(assembler));
        resources.add(createLinkFindByEmail(assembler));
        resources.add(createLinkFindByLevelsOfAccess(assembler));
        resources.add(createLinkFindByAll(assembler));
        return resources;
    }

    private static Link createLinkFindByName(PagedResourcesAssembler assembler){
        return linkTo(methodOn(UserEndpoint.class).getUserName(null, PageRequest.of(0, 10),
                assembler))
                .withRel("findByName");
    }

    private static Link createLinkFindByNameLike(PagedResourcesAssembler assembler){
        return linkTo(methodOn(UserEndpoint.class).getUserNameLike(null, PageRequest.of(0, 10),
                assembler))
                .withRel("findByNameLike");
    }

    private static Link createLinkFindByEmail(PagedResourcesAssembler assembler){
        return linkTo(methodOn(UserEndpoint.class).getUserEmail(null,
                assembler))
                .withRel("findByEmail");
    }

    private static Link createLinkFindByLevelsOfAccess(PagedResourcesAssembler assembler){
        return linkTo(methodOn(UserEndpoint.class).getUserIdNivel(null, PageRequest.of(0, 10),
                assembler))
                .withRel("findByLevelOfAccess");
    }

    private static Link createLinkFindByAll(PagedResourcesAssembler assembler){
        return linkTo(methodOn(UserEndpoint.class).listAll(PageRequest.of(0, 10),
                assembler))
                .withRel("users");
    }
}
