import React from "react";
import {connect} from "react-redux";
import {DEFAULT_PAGE_SIZE} from "../constants";
import {alertActions} from "../actions";
import {EventSuggestion} from "../components";
import {eventSuggestionService} from "../services/event-suggestion.service";

class EventSuggestionsPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      eventSuggestions: [],
      page: 0,
      size: DEFAULT_PAGE_SIZE,
      isLoading: false
    };

    this.loadEvents = this.loadEvents.bind(this);
    this.handleLoadMore = this.handleLoadMore.bind(this);
  }

  loadEvents(page = 0, size = DEFAULT_PAGE_SIZE) {
    const eventSuggestions = this.state.eventSuggestions.slice();
    eventSuggestionService.getPage(page, size)
        .then(loadedEvents => {
          this.setState({
            eventSuggestions: eventSuggestions.concat(loadedEvents),
            page,
            size,
            isLoading: false
          });
        }, error => {
          this.props.dispatch(alertActions.error(error));
        });
  }

  handleLoadMore() {
    this.loadEvents(this.state.page + 1);
  }

  componentDidMount() {
    this.loadEvents();
  }

  delete(e, id) {
    e.preventDefault();

    eventSuggestionService.delete(id)
        .then(response => {
          this.props.dispatch(alertActions.success(response.message));
        }, error => {
          this.props.dispatch(alertActions.error(error));
        });

    this.setState({
      ...this.state,
      eventSuggestions: this.state.eventSuggestions.filter(e => e.id !== id)
    });
  }

  render() {
    const {eventSuggestions} = this.state;
    const eventSuggestionViews = [];
    eventSuggestions.forEach((eventSuggestion, id) => {
      eventSuggestionViews.push(
          <EventSuggestion key={eventSuggestion.id} title={eventSuggestion.title}
                           description={eventSuggestion.description} deleteFunc={(e) => this.delete(e, id)}/>
      );
    });
    return (
        <>
          {eventSuggestionViews}
          {!this.state.isLoading ?
              <div className="text-center">
                <button className="btn btn-link" onClick={this.handleLoadMore}
                        disabled={this.state.isLoading}>Load more
                </button>
              </div>
              : null
          }
        </>
    );
  }
}

function mapStateToProps(state) {
  return {};
}

const connectedEventSuggestionsPagePage = connect(mapStateToProps)(EventSuggestionsPage);
export {connectedEventSuggestionsPagePage as EventSuggestionsPage};