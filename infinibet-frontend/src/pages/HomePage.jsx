import React from 'react';
import {connect} from 'react-redux';

import {alertActions} from '../actions';
import {eventService} from "../services/event.service";
import {DEFAULT_PAGE_SIZE} from "../constants";
import {Event} from "../components";

class HomePage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      events: [],
      page: 0,
      size: DEFAULT_PAGE_SIZE,
      isLoading: false
    };

    this.loadEvents = this.loadEvents.bind(this);
    this.handleLoadMore = this.handleLoadMore.bind(this);
  }

  loadEvents(page = 0, size = DEFAULT_PAGE_SIZE) {
    const events = this.state.events.slice();
    eventService.getPage(page, size)
        .then(loadedEvents => {
          this.setState({
            events: events.concat(loadedEvents),
            page,
            size,
            isLoading: false
          });
        }, error => {
          this.props.dispatch(alertActions.error(error));
        })
  }

  handleLoadMore() {
    this.loadEvents(this.state.page + 1);
  }

  componentDidMount() {
    this.setState({
      ...this.state,
      isLoading: true
    });
    this.loadEvents();
  }

  render() {
    const {events} = this.state;
    const eventViews = [];
    events.forEach((event, id) => {
      eventViews.push(
          <Event key={id} id={event.id} title={event.title} description={event.description}/>
      );
    });
    return (
        <>
          {eventViews}
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

const connectedHomePage = connect(mapStateToProps)(HomePage);
export {connectedHomePage as HomePage};