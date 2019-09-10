package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

class UserHateos {

    static PagedResources<User> creteLinksListAll(Page<User> users, Pageable pageable, PagedResourcesAssembler assembler){
        ControllerLinkBuilder ctrlBldr =
                linkTo(methodOn(UserEndpoint.class).listAll(pageable,
                        assembler));
        return constructLinks(ctrlBldr, users, assembler);
    }

    static PagedResources<User> creteLinksGetUserByCityLike(String city, Page<User> users, Pageable pageable,
                                                            PagedResourcesAssembler assembler){
        ControllerLinkBuilder ctrlBldr =
                linkTo(methodOn(UserEndpoint.class).getUserByCityLike(city, pageable,
                        assembler));
        return constructLinks(ctrlBldr, users, assembler);
    }

    private static PagedResources<User> constructLinks(ControllerLinkBuilder ctrlBldr, Page<User> users,
                                                       PagedResourcesAssembler assembler){
        UriComponentsBuilder builder = ctrlBldr.toUriComponentsBuilder();
        PagedResources<User> resources = assembler.toResource(users);
        resources.removeLinks();

        int pageNumber = users.getPageable().getPageNumber();

        if(pageNumber > 0){

            builder.replaceQueryParam("page", pageNumber - 1);

            Link selfLinkBefore =
                    new Link(new UriTemplate(builder.build().toString()), "usersBefore");
            resources.add(selfLinkBefore);
        }
        if(pageNumber <  users.getTotalPages() -1){
            builder.replaceQueryParam("page", pageNumber + 1);

            Link selfLinkAfter =
                    new Link(new UriTemplate(builder.build().toString()), "usersAfter");

            resources.add(selfLinkAfter);
        }
        return resources;
    }

    static void createLinkById(User user, String rel, PagedResourcesAssembler assembler){
        Link ctrlBldr = linkTo(methodOn(UserEndpoint.class).getUserById(user.getCode(), assembler)).withRel(rel);
        user.add(ctrlBldr);
    }

    static void createLink(Optional<User> user, String rel, PagedResourcesAssembler assembler){
        Link ctrlBldr = linkTo(methodOn(UserEndpoint.class).listAll(PageRequest.of(0, 10),
                assembler))
                .withRel(rel);
        user.get().add(ctrlBldr);
    }
}
